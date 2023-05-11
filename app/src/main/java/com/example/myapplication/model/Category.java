package com.example.myapplication.model;

public class Category {
    public String _id;
    public String name;
    public String status;
    public int __v;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public Category(String _id, String name, String status, int __v) {
        this._id = _id;
        this.name = name;
        this.status = status;
        this.__v = __v;
    }
}
