package railo.transformer.cfml.evaluator.impl;

import railo.transformer.cfml.evaluator.ChildEvaluator;

public final class Chartdata extends ChildEvaluator {

	@Override
	protected String getParentName() {
		return "chartseries";
	}

}