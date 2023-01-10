package uk.sky.cardealer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class File {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}
