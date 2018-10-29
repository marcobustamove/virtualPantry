package database;

public class ItemDBSchema {
    public static final class ItemTable {
        public static final String NAME = "Items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String DATE = "date";
            public static final String STATUS = "status";
            public static final String PANTRY_ID = "pantry_id";
            public static final String CATEGORY = "category";
        }
    }
}
