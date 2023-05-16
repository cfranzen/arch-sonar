model {
    archSonar = softwareSystem "Arch Sonar" {
        codeAnalyzer = container "Code Analyzer" "Scans source files and binary artefacts to analyze the target system architecture" {
            group "Orchastration" {
                runner = component "Analyzer Runner" "Reads the project configuration and puts all moving parts together"
                project = component "Project Configuration" "Manages the configuration of a software project (e.g. Resource paths, Git credentials, Maven settings)"

                runner -> project "Uses"
            }

            resScan = group "Resource Scanning" {
                resScanSpi = component "Resource Scanning SPI" "Introduces the abstraction of source or binary resources"
                resScanFs = component "File System Resource Scanning" "Scans for resources within a filesystem directory"
                resScanGit = component "Git Repo Resource Scanning" "Scans for resources within a Git repository"
                resScanZip = component "Archive Resource Scanning" "Scans for resources inside an archive file (e.g. ZIP, JAR)"

                resScanFs -> resScanSpi "Implements"
                resScanGit -> resScanSpi "Implements"
                resScanZip -> resScanSpi "Implements"
            }

            runner -> resScanFs "Uses"
            runner -> resScanGit "Uses"
            runner -> resScanZip "Uses"

            group "Resource Pre-Processing" {
                resPreSpi = component "Resource PreProcessing SPI" "Introduces the pre-processing of resources which might enhance resources or detect new ones"
                resPreMaven = component "Maven Dependency Resolver" "Reads a Maven pom.xml and adds its dependencies as additional resources"
                resPreZip = component "ZIP Archive Resolver" "Looks into an archive for additional resources"
                resPreMediaType = component "MediaType Detection" "Detectes the media type of a resource"

                resPreMaven -> resPreSpi "Implements"
                resPreZip -> resPreSpi "Implements"
                resPreMediaType -> resPreSpi "Implements"
            }

            runner -> resPreMaven "Uses"
            runner -> resPreZip "Uses"
            runner -> resPreMediaType "Uses"
            resPreZip -> resScanZip "Uses"

            group "Component Detection" {
                compSpi = component "Component Detection SPI" "Introduces a component & relation model that maps to all programming languages (e.g. modules, classes, methods)"
                compJava = component "Java Component Detection" "Analyzes .java and .class resources to detect all Java classes and methods"
                compMaven = component "Maven Module Detection" "Searches for pom.xml files and detects the root of Maven modules"

                compJava -> compSpi "Implements"
                compMaven -> compSpi "Implements"
            }

            runner -> compJava "Uses"
            runner -> compMaven "Uses"
            compJava -> resScanSpi "Uses"
            compMaven -> resScanSpi "Uses"

            group "Structure Detection" {
                structSpi = component "Structure Detection SPI" "Offers extension points to register different structure detectors"
                structSubSystem = component "Sub-System Detection" "Determines which components belong together to sub-systems or modules"
                structHierachy = component "Hierachy Detection" "Builds the inheritance hierarchy of components"
                structDepGraph = component "Dependency Graph Detection" "Builds a dependency graph of components and detects cycles"

                structSubSystem -> structSpi "Implements"
                structHierachy -> structSpi "Implements"
                structDepGraph -> structSpi "Implements"
            }

            runner -> structSubSystem "Uses"
            runner -> structHierachy "Uses"
            runner -> structDepGraph "Uses"
            structSubSystem -> compSpi "Uses"
            structHierachy -> compSpi "Uses"
            structDepGraph -> compSpi "Uses"

            group "Metrics" {
                metricsComplexity = component "Complexity" "Determines complexity (e.g. cyclomatic) of methods, classes or modules"
                metricsLOC = component "Lines of Code" "Measures lines of code of all components"
                metricsCodeCity = component "Code City View" "Renders complexity, lines of code and frequency of change of all components in a 3D plot that looks like a city"

                metricsCodeCity -> metricsComplexity "Uses"
                metricsCodeCity -> metricsLOC "Uses"
            }

            runner -> metricsComplexity "Uses"
            runner -> metricsLOC "Uses"
            runner -> metricsCodeCity "Uses"
            metricsComplexity -> resScanSpi "Uses"
            metricsLOC -> resScanSpi "Uses"
        }
    }
}