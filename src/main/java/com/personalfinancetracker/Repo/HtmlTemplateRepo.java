package com.personalfinancetracker.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personalfinancetracker.enity.HtmlFormat;

@Repository
public interface HtmlTemplateRepo extends JpaRepository<HtmlFormat, Long>{

}
