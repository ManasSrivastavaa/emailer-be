package com.manas.emailer.entity;

import org.hibernate.annotations.Nationalized;

import com.manas.emailer.dto.request.AttachmentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "data", nullable = false,length = 2097152)
    @Nationalized
    private byte[] data;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;
    
    
    public Attachment(AttachmentDTO attachmentDto) {
    	this.data = attachmentDto.data();
    	this.fileName = attachmentDto.fileName();
    	this.mimeType = attachmentDto.mimeType();
    }
}
