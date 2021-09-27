/*
 * The MIT License
 * Copyright © 2021 The National Archives
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package uk.gov.nationalarchives.pdi.step.xml;

import org.apache.commons.io.IOUtils;
import org.apache.xml.security.c14n.Canonicalizer;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

public class CanonicalStep extends BaseStep implements StepInterface {

    private static final Class<?> PKG = CanonicalStep.class;

    /**
     * This is the base step that forms that basis for all steps. You can derive from this class to implement your own
     * steps.
     *
     * @param stepMeta          The StepMeta object to run.
     * @param stepDataInterface the data object to store temporary data, database connections, caches, result sets,
     *                          hashtables etc.
     * @param copyNr            The copynumber for this step.
     * @param transMeta         The TransInfo of which the step stepMeta is part of.
     * @param trans             The (running) transformation to obtain information shared among the steps.
     */
    public CanonicalStep(final StepMeta stepMeta, final StepDataInterface stepDataInterface, final int copyNr, final TransMeta transMeta, final Trans trans) {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    @Override
    public boolean processRow(final StepMetaInterface smi, final StepDataInterface sdi) throws KettleException {
        final CanonicalStepMeta meta = (CanonicalStepMeta) smi;
        final CanonicalStepData data = (CanonicalStepData) sdi;

        final Object[] row = getRow();
        if (row == null) {
            setOutputDone();
            return false;
        }

        if (first) {
            first = false;
            data.setOutputRowMeta(getInputRowMeta().clone());
            meta.getFields(data.getOutputRowMeta(), getStepname(), null, null, this, null, null);
            data.setXmlFieldIdx(getInputRowMeta().indexOfValue(meta.getInputField()));
            data.setOutputFieldIndex(data.getOutputRowMeta().indexOfValue(meta.getOutputField()));
            org.apache.xml.security.Init.init();
            data.setCanonicalizer(getCanonicalizer());
            data.setDocumentBuilder(getDocumentBuilder());
        }
        final Object xmlFieldValue = row[data.getXmlFieldIdx()];
        if (xmlFieldValue instanceof String) {
            final CanonicalizationResult result = process((String) xmlFieldValue, data);
            if (result.hasError()) {
                putError(data.getOutputRowMeta(), row, 1L, result.getErrorMessage(), meta.getInputField(), "CanonicalStep001");
            } else {
                Object[] outputRow = RowDataUtil.resizeArray(row, data.getOutputRowMeta().size());
                outputRow[data.getOutputFieldIndex()] = result.getCanonicalXml();
                putRow(data.getOutputRowMeta(), outputRow);
            }
            return true;
        } else {
            throw new KettleException(BaseMessages.getString(PKG, "CanonicalStep.Error.XmlStringNotFound", meta.getInputField(), xmlFieldValue.getClass()));
        }
    }

    private CanonicalizationResult process(final String xmlString, final CanonicalStepData data) {
        try {
            final Document xmlDoc = createDocument(xmlString, data);
            return new CanonicalizationResult(canonicalize(xmlDoc, data));
        } catch (KettleException kex) {
            CanonicalizationResult result = new CanonicalizationResult("");
            result.setErrorMessage(kex.getMessage());
            result.setHasError(true);
            return result;
        }
    }

    private Canonicalizer getCanonicalizer() throws KettleException {
        try {
            return Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS);
        } catch (Exception e) {
            throw new KettleException(e.getMessage(), e);
        }
    }

    private DocumentBuilder getDocumentBuilder() throws KettleException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new KettleException(e.getMessage(), e);
        }
    }

    private Document createDocument(final String xmlString, final CanonicalStepData data) throws KettleException {
        try {
            return data.getDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            throw new KettleException(e.getMessage(), e);
        }
    }

    protected static String canonicalize(final Document document, final CanonicalStepData data) throws KettleException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            data.getCanonicalizer().canonicalizeSubtree(document, baos);
            return baos.toString();
        } catch (Exception e) {
            throw new KettleException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
}
