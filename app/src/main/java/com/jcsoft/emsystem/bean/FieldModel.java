package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * Created by luzefeng on 2015/1/22.
 */
public class FieldModel implements Serializable {
    private int id;
    private String name;
    private String label;
    private String field_type;
    private String category;
    private Integer position;
    private boolean required;//pc不解析
    private boolean cannot_edit;
    private String status;
    private boolean is_special_column;
    private boolean is_user_custom_column;
    private int custom_field_setting_id;
    private int custom_field_group_id;
    private String custom_column_name;

    public FieldModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIs_special_column() {
        return is_special_column;
    }

    public void setIs_special_column(boolean is_special_column) {
        this.is_special_column = is_special_column;
    }

    public boolean isIs_user_custom_column() {
        return is_user_custom_column;
    }

    public void setIs_user_custom_column(boolean is_user_custom_column) {
        this.is_user_custom_column = is_user_custom_column;
    }

    public int getCustom_field_setting_id() {
        return custom_field_setting_id;
    }

    public void setCustom_field_setting_id(int custom_field_setting_id) {
        this.custom_field_setting_id = custom_field_setting_id;
    }

    public int getCustom_field_group_id() {
        return custom_field_group_id;
    }

    public void setCustom_field_group_id(int custom_field_group_id) {
        this.custom_field_group_id = custom_field_group_id;
    }

    public String getCustom_column_name() {
        return custom_column_name;
    }

    public void setCustom_column_name(String custom_column_name) {
        this.custom_column_name = custom_column_name;
    }

    public boolean isCannot_edit() {
        return cannot_edit;
    }

    public void setCannot_edit(boolean cannot_edit) {
        this.cannot_edit = cannot_edit;
    }
}

