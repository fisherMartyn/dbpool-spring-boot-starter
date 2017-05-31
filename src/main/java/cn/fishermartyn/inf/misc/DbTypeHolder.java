package cn.fishermartyn.inf.misc;

/**
 * 保存Druid数据源的信息，默认情况下会连接主库，如果有一主多从，会随即选择一个从库建立连接池，可以通过如下方式切换：
 * 
 * </p>
 * 切换到master数据源：
 * DbTypeHolder.setMaster();
 * 
 * </p>
 * 切换到slave数据源：
 * DbTypeHolder.setSlave();
 * 
 * </p>
 * @author yujixing
 *
 */
public enum DbTypeHolder {

    MASTER, SLAVE;

    private static final ThreadLocal<DbTypeHolder> dbTypeHolder = new ThreadLocal<DbTypeHolder>() {
        @Override
        protected DbTypeHolder initialValue() {
            return DbTypeHolder.MASTER;
        }
    };

    private static void setDbType(DbTypeHolder type) {
        dbTypeHolder.set(type);
    }

    public static void setMaster() {
        DbTypeHolder.setDbType(DbTypeHolder.MASTER);
    }

    public static void setSlave() {
        DbTypeHolder.setDbType(DbTypeHolder.SLAVE);
    }

    public static DbTypeHolder getDbType() {
        return dbTypeHolder.get();
    }

}
