import org.jahia.services.content.*;
import org.jahia.registries.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.quartz.*;
import org.jahia.services.scheduler.*;
import org.jahia.services.importexport.*;

def log = log;
final String folderPath = "C:\\runtime\\carsat-nordest-novalidation";
final String siteZip = "carsat-nordest.zip";

JCRTemplate.getInstance().doExecuteWithSystemSession("root", "default", null, new JCRCallback<Object>() {
    Object doInJCR(JCRSessionWrapper session) {

        try {
            java.io.File file = new java.io.File(folderPath + "/" + "users.zip");
            log.info("users file exists: " + file.exists());
            org.jahia.services.importexport.ImportExportBaseService impService = org.jahia.registries.ServicesRegistry.getInstance().getImportExportService();
            JCRSessionWrapper systemSession = JCRSessionFactory.getInstance().getCurrentSession("default", session.getLocale(), null, true);

            if (file.exists()) {
                org.springframework.core.io.Resource resource = new org.springframework.core.io.FileSystemResource(file);
                impService.importZip("/", resource, 0, systemSession);
            }
            file = new java.io.File(folderPath + "/" + "roles.zip");
            log.info("roles file exists: " + file.exists());
            if (file.exists()) {
                org.springframework.core.io.Resource resource = new org.springframework.core.io.FileSystemResource(file);
                impService.importZip("/", resource, 0, systemSession);
            }
            file = new java.io.File(folderPath + "/" + siteZip);
            log.info("site file exists: " + file.exists());
            if (file.exists()) {
                try {
                    org.springframework.core.io.Resource resource = new org.springframework.core.io.FileSystemResource(file);
                    impService.importSiteZip(resource, systemSession);
                } catch (Exception ex) {
                    log.error("Error on import site: ", ex);
                }
            }
        } catch (Exception ex) {
            log.error("Exception in import script", ex);
        }


    }
});
