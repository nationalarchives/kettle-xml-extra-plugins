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

import org.apache.xml.security.c14n.Canonicalizer;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

import javax.xml.parsers.DocumentBuilder;

public class CanonicalStepData extends BaseStepData implements StepDataInterface {

    private Canonicalizer canon;
    private DocumentBuilder builder;
    private RowMetaInterface outputRowMeta;
    private int xmlFieldIdx;
    private int outputFieldIndex = -1;

    public void setXmlFieldIdx(final int xmlFieldIdx) {
        this.xmlFieldIdx = xmlFieldIdx;
    }

    public int getOutputFieldIndex() {
        return outputFieldIndex;
    }

    public void setOutputFieldIndex(final int outputFieldIndex) {
        this.outputFieldIndex = outputFieldIndex;
    }

    public void setCanonicalizer(final Canonicalizer canon) {
        this.canon = canon;
    }

    public Canonicalizer getCanonicalizer() {
        return canon;
    }

    public void setOutputRowMeta(final RowMetaInterface outputRowMeta) {
        this.outputRowMeta = outputRowMeta;
    }

    public RowMetaInterface getOutputRowMeta() {
        return outputRowMeta;
    }

    public void setDocumentBuilder(final DocumentBuilder builder) {
        this.builder = builder;
    }

    public DocumentBuilder getDocumentBuilder() {
        return builder;
    }

    public int getXmlFieldIdx() {
        return xmlFieldIdx;
    }
}
