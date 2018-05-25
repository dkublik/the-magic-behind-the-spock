package pl.dk.spockmagic

import org.spockframework.mock.runtime.InteractionBuilder
import org.spockframework.runtime.ErrorCollector
import org.spockframework.runtime.SpockRuntime
import org.spockframework.runtime.ValueRecorder
import org.spockframework.runtime.model.BlockKind
import org.spockframework.runtime.model.BlockMetadata
import org.spockframework.runtime.model.FeatureMetadata
import org.spockframework.runtime.model.FieldMetadata
import org.spockframework.runtime.model.SpecMetadata
import spock.lang.Specification
import spock.lang.Subject

@SpecMetadata(filename = 'MagnifyingProxySpec.groovy', line = 6)
class MagnifyingProxySpec extends Specification {

    @FieldMetadata(name = 'valueProvider', ordinal = 0, line = 8)
    private ValueProvider valueProvider
    @FieldMetadata(name = 'usageCounter', ordinal = 1, line = 9)
    private UsageCounter usageCounter
    @Subject
    @FieldMetadata(name = 'magnifyingProxy', ordinal = 2, line = 11)
    private MagnifyingProxy magnifyingProxy

    private Object $spock_initializeFields() {
        valueProvider = StubImpl('valueProvider', ValueProvider)
        usageCounter = MockImpl('usageCounter', UsageCounter)
        magnifyingProxy = new MagnifyingProxy(valueProvider, usageCounter)
    }

    @FeatureMetadata(name = 'should magnify value', ordinal = 0, line = 14,
            blocks = [
                @BlockMetadata(kind = BlockKind.SETUP, texts = []),
                @BlockMetadata(kind = BlockKind.WHEN, texts = []),
                @BlockMetadata(kind = BlockKind.THEN, texts = []),
            ],
    parameterNames = [])
    void $spock_feature_0_0() {
        ErrorCollector $spock_errorCollector = new ErrorCollector(false)
        ValueRecorder $spock_valueRecorder = new ValueRecorder()
        try {
            getSpecificationContext().getMockController().addInteraction(new InteractionBuilder(16, 13, 'valueProvider.provideValue() >> 21').addEqualTarget(valueProvider).addEqualMethodName('provideValue').setArgListKind(true).addConstantResponse(21).build())
            getSpecificationContext().getMockController().enterScope()
            getSpecificationContext().getMockController().addInteraction(new InteractionBuilder(23, 13, '1 * usageCounter.increase()').setFixedCount(1).addEqualTarget(usageCounter).addEqualMethodName('increase').setArgListKind(true).build())
            Integer result = magnifyingProxy.provideMagnifiedValue()
            getSpecificationContext().getMockController().leaveScope()
            try {
                SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), 'result == 210', 22, 13, null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(2), $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(0), result) == $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(1), 210)))
            }
            catch (Throwable throwable) {
                SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, 'result == 210', 22, 13, null, throwable)}
            finally {
            }
            getSpecificationContext().getMockController().leaveScope()
        }
        finally {
            $spock_errorCollector.validateCollectedErrors()}
    }

}
