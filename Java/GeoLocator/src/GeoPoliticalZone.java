    public enum GeoPoliticalZone {
        NORTH_CENTRAL("Benue", "FCT", "Kogi", "Kwara", "Nasarawa", "Niger", "Plateau"),
        NORTH_EAST("Adamawa", "Bauchi", "Borno", "Gombe", "Taraba", "Yobe"),
        NORTH_WEST("KADUNA", "KATSINA", "KOBE", "KEBBI", "SOKOTO", "JIGAWA", "ZAMFARA"),
        SOUTH_EAST("ABIA", "ANAMBRA", "EBONYI", "ENUGU", "IMO"),
        SOUTH_SOUTH("AKWA-IBOM", "BAYELSA", "CROSS-RIVER", "DELTA", "EDO", "RIVERS"),
        SOUTH_WEST("EKITI", "LAGOS", "OSUN", "ONDO", "OGUN", "OYO");

        private final String[] states;

        GeoPoliticalZone(String... states) {
            this.states = states;
        }

        public String[] getStates(String... states) {
            return states;
        }

        public boolean checkState(String user_state) {
            for (String state : states) {
                return user_state == state;
            }
            return false;
        }
    }


