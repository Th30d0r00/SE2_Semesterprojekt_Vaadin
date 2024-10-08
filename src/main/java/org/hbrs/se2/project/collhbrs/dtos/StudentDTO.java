package org.hbrs.se2.project.collhbrs.dtos;

import java.time.LocalDate;

public interface StudentDTO
{
    int getId();
    void setId(int id);
    public String getLastname();

    void setLastname(String lastname);

    public String getFirstname();

    void setFirstname(String firstname);

    public LocalDate getBirthday();

    void setBirthday(LocalDate birthday);

    int getFachsemester();

    void setFachsemester(int fachsemester);

}
