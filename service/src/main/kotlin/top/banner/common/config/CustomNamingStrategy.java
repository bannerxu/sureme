package top.banner.common.config;

import org.hibernate.HibernateException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

public class CustomNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        if(source == null) {
            throw new HibernateException("Entity naming information was not provided.");
        } else {
            String tableName = super.transformEntityName(source.getEntityNaming());
            if(tableName == null) {
                throw new HibernateException("Could not determine primary table name for entity");
            } else {
                tableName = "tb" + tableName;
                return super.toIdentifier(tableName, source.getBuildingContext());
            }
        }
    }
}