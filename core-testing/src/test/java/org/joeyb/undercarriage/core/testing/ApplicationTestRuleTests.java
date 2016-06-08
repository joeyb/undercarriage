package org.joeyb.undercarriage.core.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.joeyb.undercarriage.core.ApplicationBase;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;

public class ApplicationTestRuleTests {

    private final ApplicationTestRule<ConfigSection, MockApplication> applicationTestRule =
            new ApplicationTestRule<>(MockApplication::new);

    @Rule
    public final RuleChain ruleChain = RuleChain.outerRule(new FakeTestRule(applicationTestRule))
            .around(applicationTestRule);

    @Test
    public void applicationShouldBeConfiguredAndStartedButNotStopped() {
        assertThat(applicationTestRule.application()).isNotNull();
        assertThat(applicationTestRule.application().isConfigured()).isTrue();
        assertThat(applicationTestRule.application().isStarted()).isTrue();
        assertThat(applicationTestRule.application().isStopped()).isFalse();
    }

    /**
     * {@code FakeTestRule} is used to test the state of the {@link ApplicationTestRule} instance before and after the
     * test has executed.
     */
    private static class FakeTestRule extends ExternalResource {

        private final ApplicationTestRule<ConfigSection, MockApplication> applicationTestRule;

        FakeTestRule(ApplicationTestRule<ConfigSection, MockApplication> applicationTestRule) {
            this.applicationTestRule = applicationTestRule;
        }

        @Override
        protected void before() throws Throwable {
            // Make sure the rule's application reference is null before the test has started.
            assertThat(applicationTestRule.application()).isNull();
        }

        @Override
        protected void after() {
            // Make sure the rule's application reference is null after the test is over.
            assertThat(applicationTestRule.application()).isNull();
        }
    }

    private static class MockApplication extends ApplicationBase<ConfigSection> {

        MockApplication() {
            super(new ManualConfigContext<>(mock(ConfigSection.class)));
        }
    }
}
