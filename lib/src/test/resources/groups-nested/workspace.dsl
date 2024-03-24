workspace {

    model {
        properties {
            structurizr.groupSeparator /
        }

        group "Organisation" {
            group "Department A" {
                a = softwareSystem "A" {
                    group "Capability 1" {
                        group "Service A" {
                            container "A API"
                            container "A Database"
                        }
                        group "Service B" {
                            container "B API"
                            container "B Database"
                        }
                    }
                }
            }

            group "Department B" {
                b = softwareSystem "B"
            }

            c = softwareSystem "C"
        }

        group "Enterprise" {
            group "Department A" {
                group "Team 1" {
                    d = softwareSystem "D"
                }
            }
        }
    }

    views {
        systemLandscape {
            include *
            autolayout
        }
    }

}
