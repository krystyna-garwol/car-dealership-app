package uk.sky.cardealer.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.sky.cardealer.model.File;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    public String uploadImage(MultipartFile file, String newName) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", file.getSize());
        metadata.put("originalName", file.getOriginalFilename());

        Object fileId = gridFsTemplate.store(file.getInputStream(), newName, file.getContentType(), metadata);

        return fileId.toString();
    }

    public File downloadImage(String id) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        File file = new File();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            file.setFilename(gridFSFile.getFilename());
            file.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            file.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
            file.setFile(IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));
        }

        return file;
    }
}
