workspace {

    model {
        softwareSystem = softwareSystem "Software System" "My software system."
    }

    views {
        properties {
            // default for all
            d2.title_position top-left
        }

        systemContext softwareSystem "Default" {
            include *
            autoLayout
        }

        systemContext softwareSystem "TopLeft" {
            include *
            autoLayout
            properties {
                d2.title_position bottom-left
            }
        }

        systemContext softwareSystem "TopRight" {
            include *
            autoLayout
            properties {
                d2.title_position top-right
            }
        }
    }
}