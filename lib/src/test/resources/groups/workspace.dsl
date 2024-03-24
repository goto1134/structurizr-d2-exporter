workspace {

    model {
        properties {
                "structurizr.groupSeparator" "/"
        }
        a = softwareSystem "A"

        group "Group 1" {
            b = softwareSystem "B"
        }

        group "Enterprise" {
            c = softwareSystem "C"

            group "Group 2" {
                d = softwareSystem "D" {
                    e = container "E"

                    group "Group 3" {
                        f = container "F" {
                            g = component "G"

                            group "Group 4" {
                                h = component "H"
                            }
                        }
                    }
                }
            }
        }

        a -> b
        b -> c
        c -> e
        c -> g
        c -> h

    }

    views {
        systemlandscape "SystemLandscape" {
            include *
            autolayout
        }

        container d "Containers" {
            include *
            autolayout
        }

        component f "Components" {
            include *
            autolayout
        }
    }

}
