/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.kixe;

import ec.tstoolkit.timeseries.regression.TsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.utilities.DefaultNameValidator;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Deutsche Bundesbank
 */
public class UncPerTest {

    public UncPerTest() {
    }
    private final String VALIDATOR = ",= +-";
    private final double[] i1Data = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8,
        92.4, 95.6, 98.8, 103.5, 102.0, 107.4, 110.6, 113.9,
        117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7,
        149.0};
    private final TsVariables indices = new TsVariables("i", new DefaultNameValidator(VALIDATOR));
    private final TsVariables weights = new TsVariables("w", new DefaultNameValidator(VALIDATOR));

    @Test
    public void testCompute_UnchainIndex() {
        System.out.println("Start Compute_UnchainIndex");
        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2007, 3, i1Data, true)));

        String formula = "UNC.per,i1";
        UncPer instance = new UncPer();
        TsData expResult = new TsData(TsFrequency.Quarterly, 2007, 3, new double[]{100, 103.99467,
            106.39148, 106.12517, 108.25566, 105.90406,
            109.71710, 111.68512, 113.65314, 103.46320,
            106.92641, 112.01299, 110.38961, 105.29412,
            108.43137, 111.66667, 115.19608, 103.06383,
            106.21277, 109.27660, 112.68085, 102.87009,
            106.19335, 109.29003, 112.53776}, true);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        System.out.println("End Compute_UnchainIndex");
    }

    @Test
    public void testCompute_UnchainWeight() {
        System.out.println("Start Compute_UnchainWeight");
        indices.clear();
        weights.clear();
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2007, 3, i1Data, true)));

        String formula = "UNC.per,w1";
        UncPer instance = new UncPer();
        TsData expResult = new TsData(TsFrequency.Quarterly, 2007, 3, new double[]{100, 103.99467,
            106.39148, 106.12517, 108.25566, 105.90406,
            109.71710, 111.68512, 113.65314, 103.46320,
            106.92641, 112.01299, 110.38961, 105.29412,
            108.43137, 111.66667, 115.19608, 103.06383,
            106.21277, 109.27660, 112.68085, 102.87009,
            106.19335, 109.29003, 112.53776}, true);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        System.out.println("End Compute_UnchainWeight");
    }

    @Test
    public void testCompute_UnchainNotFound() {
        System.out.println("Start Compute_UnchainNotFound");
        indices.clear();
        weights.clear();

        String formula = "UNC.per,w1";
        UncPer instance = new UncPer();

        TsData expResultTsData = null;
        TsData resultTsData = instance.compute(formula, indices, weights);

        String expResultErrorMessage = "w1 doesn't exist";
        TsDataAsserter.assertTsDataEquals(expResultTsData, resultTsData, 0.005);
        assertEquals(expResultErrorMessage, instance.getErrorMessage());

        System.out.println("End Compute_UnchainNotFound");
    }

    @Test
    public void testCompute_UnchainWrongFormula() {
        System.out.println("Start Compute_UnchainNotFound");
        indices.clear();
        weights.clear();

        String formula = "UNC.per,w1,2005";
        UncPer instance = new UncPer();

        TsData expResultTsData = null;
        TsData resultTsData = instance.compute(formula, indices, weights);

        String expResultErrorMessage = "Formula doesn't match required syntax";
        TsDataAsserter.assertTsDataEquals(expResultTsData, resultTsData, 0.005);
        assertEquals(expResultErrorMessage, instance.getErrorMessage());

        System.out.println("End Compute_UnchainNotFound");
    }

    @Test
    public void testGetValidControlCharacter() {
        System.out.println("getValidControlCharacter");
        UncPer instance = new UncPer();
        String[] expResult = {"unc.per"};
        String[] result = instance.getValidControlCharacter();
        assertArrayEquals(expResult, result);
    }

}
