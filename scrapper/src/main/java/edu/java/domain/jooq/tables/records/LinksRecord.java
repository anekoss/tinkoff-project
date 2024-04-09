/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.records;


import edu.java.domain.jooq.tables.Links;

import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


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
public class LinksRecord extends UpdatableRecordImpl<LinksRecord> implements Record5<Long, String, String, LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINKS.ID</code>.
     */
    public void setId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINKS.ID</code>.
     */
    @Nullable
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINKS.URI</code>.
     */
    public void setUri(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINKS.URI</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUri() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINKS.LINK_TYPE</code>.
     */
    public void setLinkType(@NotNull String value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINKS.LINK_TYPE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getLinkType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>LINKS.UPDATED_AT</code>.
     */
    public void setUpdatedAt(@NotNull LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINKS.UPDATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>LINKS.CHECKED_AT</code>.
     */
    public void setCheckedAt(@NotNull LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>LINKS.CHECKED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public LocalDateTime getCheckedAt() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row5<Long, String, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row5<Long, String, String, LocalDateTime, LocalDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Links.LINKS.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Links.LINKS.URI;
    }

    @Override
    @NotNull
    public Field<String> field3() {
        return Links.LINKS.LINK_TYPE;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field4() {
        return Links.LINKS.UPDATED_AT;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field5() {
        return Links.LINKS.CHECKED_AT;
    }

    @Override
    @Nullable
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUri();
    }

    @Override
    @NotNull
    public String component3() {
        return getLinkType();
    }

    @Override
    @NotNull
    public LocalDateTime component4() {
        return getUpdatedAt();
    }

    @Override
    @NotNull
    public LocalDateTime component5() {
        return getCheckedAt();
    }

    @Override
    @Nullable
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUri();
    }

    @Override
    @NotNull
    public String value3() {
        return getLinkType();
    }

    @Override
    @NotNull
    public LocalDateTime value4() {
        return getUpdatedAt();
    }

    @Override
    @NotNull
    public LocalDateTime value5() {
        return getCheckedAt();
    }

    @Override
    @NotNull
    public LinksRecord value1(@Nullable Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public LinksRecord value2(@NotNull String value) {
        setUri(value);
        return this;
    }

    @Override
    @NotNull
    public LinksRecord value3(@NotNull String value) {
        setLinkType(value);
        return this;
    }

    @Override
    @NotNull
    public LinksRecord value4(@NotNull LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinksRecord value5(@NotNull LocalDateTime value) {
        setCheckedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinksRecord values(@Nullable Long value1, @NotNull String value2, @NotNull String value3, @NotNull LocalDateTime value4, @NotNull LocalDateTime value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinksRecord
     */
    public LinksRecord() {
        super(Links.LINKS);
    }

    /**
     * Create a detached, initialised LinksRecord
     */
    @ConstructorProperties({ "id", "uri", "linkType", "updatedAt", "checkedAt" })
    public LinksRecord(@Nullable Long id, @NotNull String uri, @NotNull String linkType, @NotNull LocalDateTime updatedAt, @NotNull LocalDateTime checkedAt) {
        super(Links.LINKS);

        setId(id);
        setUri(uri);
        setLinkType(linkType);
        setUpdatedAt(updatedAt);
        setCheckedAt(checkedAt);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinksRecord
     */
    public LinksRecord(edu.java.domain.jooq.tables.pojos.Links value) {
        super(Links.LINKS);

        if (value != null) {
            setId(value.getId());
            setUri(value.getUri());
            setLinkType(value.getLinkType());
            setUpdatedAt(value.getUpdatedAt());
            setCheckedAt(value.getCheckedAt());
            resetChangedOnNotNull();
        }
    }
}
