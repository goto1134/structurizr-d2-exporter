workspace {

    model {
        user = person "User"
        softwareSystem = softwareSystem "Software System" "My software system."
        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem "SystemContext-default" {
            include *
            autoLayout
        }

        systemContext softwareSystem "SystemContext-c4" {
            properties {
                d2.use_c4_person true
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