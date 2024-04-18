package org.example.springminiproject.Model.FileModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String filename;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
}
