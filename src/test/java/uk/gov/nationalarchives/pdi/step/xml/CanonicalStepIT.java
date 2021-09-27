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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaSerializable;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.trans.RowStepCollector;
import org.pentaho.di.trans.TransMeta;

import org.pentaho.di.trans.TransTestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CanonicalStepIT {

    static final String STEP_NAME = "Integration test for Canonical XML step";

    @BeforeAll
    public static void setUpBeforeClass() throws KettleException {
        KettleEnvironment.init(false);
    }

    @Test
    public void testSuccess() throws KettleException {
        final TransMeta tm = TransTestFactory.generateTestTransformationError(new Variables(), getTestMeta(), STEP_NAME);
        final Map<String, RowStepCollector> result = TransTestFactory.executeTestTransformationError(tm, TransTestFactory.INJECTOR_STEPNAME,
                STEP_NAME, TransTestFactory.DUMMY_STEPNAME, TransTestFactory.ERROR_STEPNAME, getValidInputData());
        assertEquals(0, result.get(STEP_NAME).getRowsError().size());
    }

    @Test
    public void testException() throws KettleException {
        final TransMeta tm = TransTestFactory.generateTestTransformationError(new Variables(), getTestMeta(), STEP_NAME);
        assertThrows(KettleException.class, () -> {
            TransTestFactory.executeTestTransformationError(tm, TransTestFactory.INJECTOR_STEPNAME,
                    STEP_NAME, TransTestFactory.DUMMY_STEPNAME, TransTestFactory.ERROR_STEPNAME, getInvalidInputData());
        });
    }

    private List<RowMetaAndData> getValidInputData() {
        final List<RowMetaAndData> retval = new ArrayList<>();
        final RowMetaInterface rowMeta = new RowMeta();
        rowMeta.addValueMeta(new ValueMetaSerializable("xml_string"));
        retval.add(new RowMetaAndData(rowMeta, "<doc>test &#38;</doc>"));
        return retval;
    }

    private List<RowMetaAndData> getInvalidInputData() {
        final List<RowMetaAndData> retval = new ArrayList<>();
        final RowMetaInterface rowMeta = new RowMeta();
        rowMeta.addValueMeta(new ValueMetaSerializable("xml_string"));
        retval.add(new RowMetaAndData(rowMeta, 123));
        return retval;
    }

    private CanonicalStepMeta getTestMeta() {
        final CanonicalStepMeta meta = new CanonicalStepMeta();
        meta.setInputField("xml_string");
        meta.setOutputField("canonical_xml");
        return meta;
    }
}
