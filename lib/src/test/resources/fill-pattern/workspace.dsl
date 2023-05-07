workspace {

    model {
        user = person "User" "A user of my software system."
        softwareSystem = softwareSystem "Software System" "My software system."
        user -> softwareSystem "Uses"
    }

    views {
        properties {
            d2.fill_pattern lines
        }

        systemContext softwareSystem "SystemContext" {
            include *
            autoLayout
        }

        styles {
            element "Software System" {
                properties {
                    d2.fill_pattern dots
                }
            }
        }
    }
}