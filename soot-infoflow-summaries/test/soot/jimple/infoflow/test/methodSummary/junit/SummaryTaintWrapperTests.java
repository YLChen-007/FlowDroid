package soot.jimple.infoflow.test.methodSummary.junit;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;

import soot.jimple.infoflow.AbstractInfoflow;
import soot.jimple.infoflow.IInfoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.entryPointCreators.DefaultEntryPointCreator;
import soot.jimple.infoflow.methodSummary.data.provider.EagerSummaryProvider;
import soot.jimple.infoflow.methodSummary.taintWrappers.TaintWrapperFactory;

public abstract class SummaryTaintWrapperTests extends BaseSummaryTaintWrapperTests {

	@Ignore("kill flow")
	@Test(timeout = 30000)
	public void noFlow1() {
		testNoFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void noFlow1()>");
	}

	@Ignore("kill flow")
	@Test(timeout = 30000)
	public void noFlow2() {
		testNoFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void noFlow2()>");
	}

	@Test(timeout = 30000)
	public void flow1() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void flow1()>");
	}

	@Test(timeout = 30000)
	public void flow2() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void flow2()>");
	}

	@Test(timeout = 30000)
	public void paraReturnFlow() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraReturnFlow()>");
	}

	@Test(timeout = 30000)
	public void paraFieldSwapFieldReturnFlow() {
		testFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraFieldSwapFieldReturnFlow()>");
	}

	@Test(timeout = 30000)
	public void paraFieldFieldReturnFlow() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraFieldFieldReturnFlow()>");
	}

	@Test(timeout = 30000)
	public void paraReturnFlowInterface() {
		testFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraReturnFlowOverInterface()>");
	}

	@Test(timeout = 30000)
	public void paraFieldSwapFieldReturnFlowInterface() {
		testFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraFieldSwapFieldReturnFlowOverInterface()>");
	}

	@Test // (timeout = 30000)
	public void paraFieldFieldReturnFlowInterface() {
		testFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraFieldFieldReturnFlowOverInterface()>");
	}

	@Test(timeout = 30000)
	public void paraToParaFlow() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void paraToParaFlow()>");
	}

	@Test(timeout = 30000)
	public void fieldToParaFlow() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void fieldToParaFlow()>");
	}

	@Test(timeout = 30000)
	public void apl3NoFlow() {
		testNoFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void apl3NoFlow()>");
	}

	@Test(timeout = 30000)
	public void apl3Flow() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void apl3Flow()>");
	}

	@Test
	public void gapFlow1() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void gapFlow1()>");
	}

	@Test(timeout = 30000)
	public void gapFlow2() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void gapFlow2()>");
	}

	@Test(timeout = 30000)
	@Ignore // there is no ordering of same-level flows
	public void shiftTest() {
		testNoFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void shiftTest()>");
	}

	@Test(timeout = 30000)
	public void gapFlowUserCode1() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void gapFlowUserCode1()>");
	}

	@Test(timeout = 30000)
	public void transferStringThroughDataClass1() {
		testFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void transferStringThroughDataClass1()>");
	}

	@Test(timeout = 30000)
	public void transferStringThroughDataClass2() {
		testNoFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void transferStringThroughDataClass2()>");
	}

	@Test(timeout = 30000)
	public void storeStringInGapClass() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void storeStringInGapClass()>");
	}

	@Test(timeout = 30000)
	public void storeAliasInGapClass() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void storeAliasInGapClass()>");
	}

	@Test(timeout = 30000)
	public void storeAliasInGapClass2() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void storeAliasInGapClass2()>");
	}

	@Test
	public void storeAliasInSummaryClass() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void storeAliasInSummaryClass()>");
	}

	@Test(timeout = 30000)
	public void getLength() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void getLength()>");
	}

	@Test
	public void gapToGap() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void gapToGap()>");
	}

	@Test(timeout = 3000000)
	public void callToCall() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void callToCall()>");
	}

	@Test(timeout = 30000)
	public void objectOutputStream1() {
		testNoFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void objectOutputStream1()>");
	}

	@Test(timeout = 30000)
	public void objectOutputStream2() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void objectOutputStream2()>");
	}

	@Test(timeout = 30000)
	public void killTaint1() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void killTaint1()>");
	}

	@Test(timeout = 30000)
	public void killTaint2() {
		testNoFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void killTaint2()>");
	}

	@Test // (timeout = 30000)
	public void taintedFieldToString() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void taintedFieldToString()>");
	}

	@Test(timeout = 30000)
	public void bigIntegerToString() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void bigIntegerToString()>");
	}

	@Test
	public void mapToString() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void mapToString()>");
	}

	@Test
	public void iterator() {
		testFlowForMethod("<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void iterator()>");
	}

	@Test(timeout = 30000)
	public void noPropagationOverUnhandledCallee() {
		testNoFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void noPropagationOverUnhandledCallee()>");
	}

	@Test(timeout = 30000)
	public void identityIsStillAppliedOnUnhandledMethodButExclusiveClass() {
		testFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void identityIsStillAppliedOnUnhandledMethodButExclusiveClass()>");
	}

	@Test(timeout = 30000)
	public void matchGapReturnOnlyWithReturnTaints() {
		testNoFlowForMethod(
				"<soot.jimple.infoflow.test.methodSummary.ApiClassClient: void matchGapReturnOnlyWithReturnTaints()>");
	}

	@Test
	public void testAllSummaries() throws URISyntaxException, IOException {
		EagerSummaryProvider provider = new EagerSummaryProvider(TaintWrapperFactory.DEFAULT_SUMMARY_DIR);
		assertFalse(provider.hasLoadingErrors());
	}

}
