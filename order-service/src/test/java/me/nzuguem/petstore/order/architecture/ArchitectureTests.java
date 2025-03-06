package me.nzuguem.petstore.order.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import me.nzuguem.petstore.shared.api.docs.annotations.DomainService;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "me.nzuguem.petstore.order",
        importOptions = { ImportOption.DoNotIncludeTests.class })
public class ArchitectureTests {

    @ArchTest
    static final ArchRule domainShouldHaveNoDependencies =
            noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideOutsideOfPackages(
                            "..domain..", "..java..",
                            "..shared.api.docs.annotations..", "..shared.api.order.exceptions..",
                            "..shared.api.order.models..", "..lombok..",
                            "..jakarta..");

    @ArchTest
    static final ArchRule domainServicesShouldBeInServicesPackage =
            noClasses()
                    .that()
                    .resideOutsideOfPackage("..domain.services..")
                    .should()
                    .beAnnotatedWith(DomainService.class);
}
