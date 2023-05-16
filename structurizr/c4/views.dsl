views {
    container archSonar "ArchSonarContainersView" {
        include *
        autolayout tb
    }

    component archSonar.codeAnalyzer "CodeAnalyzerComponentsView" {
        include *
        autolayout tb
    }
}