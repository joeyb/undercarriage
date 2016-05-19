package org.joeyb.undercarriage.example;

import org.immutables.value.Value;

@Value.Immutable
public interface ExampleConfig extends ExampleConfigSection {

    String someOtherSetting();
}
