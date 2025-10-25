package com.manas.emailer.config.db.naming;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DBNameingStrategy implements PhysicalNamingStrategy {

    private static final String PREFIX = "tbl_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return Identifier.toIdentifier(PREFIX + name.getText().toLowerCase());
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) { return name; }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) { return name; }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) { return name; }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) { return name; }
}
