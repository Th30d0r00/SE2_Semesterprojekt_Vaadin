package org.hbrs.se2.project.collhbrs.dtos.impl;


import org.hbrs.se2.project.collhbrs.dtos.AnzeigeDTO;
import org.hbrs.se2.project.collhbrs.dtos.CompanyDTO;

import java.time.LocalDateTime;

public class AnzeigeDTOImpl implements AnzeigeDTO {
    private int id;
    private String jobTitle;
    private String standort;
    private String jobType;
    private CompanyDTO company;
    private String jobDescription;
    private LocalDateTime publicationDate;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String getStandort() {
        return standort;
    }

    @Override
    public void setStandort(String standort) {
        this.standort = standort;
    }

    @Override
    public String getJobType() {
        return jobType;
    }

    @Override
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public CompanyDTO getCompany() {
        return company;
    }

    @Override
    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    @Override
    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }
}
