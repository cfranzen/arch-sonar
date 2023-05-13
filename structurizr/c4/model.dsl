model {
    archSonar = softwareSystem "Arch Sonar" {
        codeAnalyzer = container "Code Analyzer" "Scans source files and binary artefacts to analyze the target systems architecture" {
            resSpi = component "Resource Scanning SPI"
            resFs = component "File System Resource Scanning"
            resGit = component "Git Repo Resource Scanning"

            resFs -> resSpi "Implements"
            resGit -> resSpi "Implements"
        }
    }
}