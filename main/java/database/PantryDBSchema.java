package database;

public class PantryDBSchema
{
    public static final class PantryTable
    {
        public static final String NAME = "Pantries";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
        }
    }

    public static final class ItemTable {
        public static final String NAME = "Items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String DATE = "date";
            public static final String STATUS = "status";
            public static final String PANTRY_ID = "pantry_id";
            public static final String CATEGORY = "category";
            public static final String IMG_REF = "img_ref";
        }
    }
}
