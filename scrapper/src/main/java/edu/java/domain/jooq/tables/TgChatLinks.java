/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables;


import edu.java.domain.jooq.DefaultSchema;
import edu.java.domain.jooq.Keys;
import edu.java.domain.jooq.tables.records.TgChatLinksRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class TgChatLinks extends TableImpl<TgChatLinksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>TG_CHAT_LINKS</code>
     */
    public static final TgChatLinks TG_CHAT_LINKS = new TgChatLinks();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<TgChatLinksRecord> getRecordType() {
        return TgChatLinksRecord.class;
    }

    /**
     * The column <code>TG_CHAT_LINKS.ID</code>.
     */
    public final TableField<TgChatLinksRecord, Long> ID = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>TG_CHAT_LINKS.TG_CHAT_ID</code>.
     */
    public final TableField<TgChatLinksRecord, Long> TG_CHAT_ID = createField(DSL.name("TG_CHAT_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>TG_CHAT_LINKS.LINK_ID</code>.
     */
    public final TableField<TgChatLinksRecord, Long> LINK_ID = createField(DSL.name("LINK_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    private TgChatLinks(Name alias, Table<TgChatLinksRecord> aliased) {
        this(alias, aliased, null);
    }

    private TgChatLinks(Name alias, Table<TgChatLinksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>TG_CHAT_LINKS</code> table reference
     */
    public TgChatLinks(String alias) {
        this(DSL.name(alias), TG_CHAT_LINKS);
    }

    /**
     * Create an aliased <code>TG_CHAT_LINKS</code> table reference
     */
    public TgChatLinks(Name alias) {
        this(alias, TG_CHAT_LINKS);
    }

    /**
     * Create a <code>TG_CHAT_LINKS</code> table reference
     */
    public TgChatLinks() {
        this(DSL.name("TG_CHAT_LINKS"), null);
    }

    public <O extends Record> TgChatLinks(Table<O> child, ForeignKey<O, TgChatLinksRecord> key) {
        super(child, key, TG_CHAT_LINKS);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public Identity<TgChatLinksRecord, Long> getIdentity() {
        return (Identity<TgChatLinksRecord, Long>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<TgChatLinksRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_A;
    }

    @Override
    @NotNull
    public List<UniqueKey<TgChatLinksRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_AABE);
    }

    @Override
    @NotNull
    public List<ForeignKey<TgChatLinksRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_AA, Keys.CONSTRAINT_AAB);
    }

    private transient TgChats _tgChats;
    private transient Links _links;

    /**
     * Get the implicit join path to the <code>PUBLIC.TG_CHATS</code> table.
     */
    public TgChats tgChats() {
        if (_tgChats == null)
            _tgChats = new TgChats(this, Keys.CONSTRAINT_AA);

        return _tgChats;
    }

    /**
     * Get the implicit join path to the <code>PUBLIC.LINKS</code> table.
     */
    public Links links() {
        if (_links == null)
            _links = new Links(this, Keys.CONSTRAINT_AAB);

        return _links;
    }

    @Override
    @NotNull
    public TgChatLinks as(String alias) {
        return new TgChatLinks(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public TgChatLinks as(Name alias) {
        return new TgChatLinks(alias, this);
    }

    @Override
    @NotNull
    public TgChatLinks as(Table<?> alias) {
        return new TgChatLinks(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChatLinks rename(String name) {
        return new TgChatLinks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChatLinks rename(Name name) {
        return new TgChatLinks(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChatLinks rename(Table<?> name) {
        return new TgChatLinks(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, Long, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Long, ? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
