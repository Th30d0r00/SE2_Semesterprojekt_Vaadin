package org.hbrs.se2.project.collhbrs.dtos.impl;

import org.hbrs.se2.project.collhbrs.dtos.StudentDTO;

import java.time.LocalDate;

public class StudentDTOImpl implements StudentDTO
{
    private int id;
    private String firstname;
    private String lastname;
    private LocalDate birthday;
    private int fachsemester;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public int getFachsemester() {
        return fachsemester;
    }
    @Override
    public void setFachsemester(int fachsemester) {
        this.fachsemester = fachsemester;
    }
}
