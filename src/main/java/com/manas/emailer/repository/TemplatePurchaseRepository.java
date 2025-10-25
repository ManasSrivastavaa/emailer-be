package com.manas.emailer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manas.emailer.entity.TemplatePurchase;
import com.manas.emailer.entity.embeddable.UserTemplateId;

public interface TemplatePurchaseRepository  extends JpaRepository<TemplatePurchase,UserTemplateId>{

}
