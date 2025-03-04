workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" "My software system."
        user -> softwareSystem "Uses"
    }

    views {
        properties {
            d2.use_c4_person true
        }

        systemContext softwareSystem "SystemContext-default" {
            include *
            autoLayout
        }

        systemContext softwareSystem "SystemContext-disabled" {
            properties {
                d2.use_c4_person false
            }
            include *
            autoLayout
        }

        styles {
            element "Person" {
                shape Person
            }
        }
    }
}