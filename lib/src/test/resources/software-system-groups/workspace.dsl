workspace {
    model {
        group "Line of Business C" {
            ss_c = softwareSystem "System C" {
            }
        }
        group "Line of Business B" {
            ss_b = softwareSystem "System B" {
            }
        }
        group "Line of Business A" {
            ss_a = softwareSystem "System A" {
                c1 = container "Container A1" {
                   -> ss_b "Uses" "HTTP"
                   -> ss_c "Uses" "HTTP"
                }
            }
        }
    }
    views {
        systemContext ss_a "ss_a_Context" "Software System A - Context" {
            include *
        }
        container ss_a "ss_a_Containers" "Software System A - Containers" {
            include *
        }
        styles {
            element "Group:Line of Business A" {
                background "#B0D4CA"
            }
            element "Group:Line of Business B" {
                background "#AA8C65"
            }
            element "Group:Line of Business C" {
                background "#577E7D"
            }
        }
    }
}
