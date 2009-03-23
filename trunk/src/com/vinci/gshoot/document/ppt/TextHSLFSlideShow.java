package com.vinci.gshoot.document.ppt;

import org.apache.poi.hslf.EncryptedSlideShow;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.exceptions.EncryptedPowerPointFileException;
import org.apache.poi.hslf.record.CurrentUserAtom;
import org.apache.poi.hslf.record.PersistPtrHolder;
import org.apache.poi.hslf.record.PersistRecord;
import org.apache.poi.hslf.record.Record;
import org.apache.poi.hslf.record.UserEditAtom;
import org.apache.poi.hslf.usermodel.ObjectData;
import org.apache.poi.hslf.usermodel.PictureData;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class TextHSLFSlideShow extends HSLFSlideShow {

    // For logging
    private POILogger logger = POILogFactory.getLogger(this.getClass());

    // Holds metadata on where things are in our document
    private CurrentUserAtom currentUser;

    // Low level contents of the file
    private byte[] _docstream;

    /**
     * Returns the underlying POIFSFileSystem for the document
     * that is open.
     */
    protected POIFSFileSystem getPOIFSFileSystem() {
        return filesystem;
    }

    public TextHSLFSlideShow(String fileName) throws IOException {
        this(new FileInputStream(fileName));
    }

    public TextHSLFSlideShow(InputStream inputStream) throws IOException {
        //do Ole stuff
        this(new POIFSFileSystem(inputStream));
    }

    public TextHSLFSlideShow(POIFSFileSystem filesystem) throws IOException {
        this(filesystem.getRoot(), filesystem);
    }

    public TextHSLFSlideShow(DirectoryNode dir, POIFSFileSystem filesystem) throws IOException {
        super(dir, filesystem);

        // First up, grab the "Current User" stream
        // We need this before we can detect Encrypted Documents
        readCurrentUserStream();

        // Next up, grab the data that makes up the
        //  PowerPoint stream
        readPowerPointStream();

        // Check to see if we have an encrypted document,
        //  bailing out if we do
        boolean encrypted = EncryptedSlideShow.checkIfEncrypted(this);
        if (encrypted) {
            throw new EncryptedPowerPointFileException("Encrypted PowerPoint files are not supported");
        }

        // Now, build records based on the PowerPoint stream
//        buildRecords();

        // Look for Property Streams:
        readProperties();

        // Look for any other streams
        //readOtherStreams();

        // Look for Picture Streams:
        //readPictures();
    }

    private void readPowerPointStream() throws IOException {
        // Get the main document stream
        DocumentEntry docProps =
                (DocumentEntry) directory.getEntry("PowerPoint Document");

        // Grab the document stream
        _docstream = new byte[docProps.getSize()];
        directory.createDocumentInputStream("PowerPoint Document").read(_docstream);
    }

    private Record[] read(byte[] docstream, int usrOffset) {
        ArrayList lst = new ArrayList();
        HashMap offset2id = new HashMap();
        while (usrOffset != 0) {
            UserEditAtom usr = (UserEditAtom) Record.buildRecordAtOffset(docstream, usrOffset);
            lst.add(new Integer(usrOffset));
            int psrOffset = usr.getPersistPointersOffset();

            PersistPtrHolder ptr = (PersistPtrHolder) Record.buildRecordAtOffset(docstream, psrOffset);
            lst.add(new Integer(psrOffset));
            Hashtable entries = ptr.getSlideLocationsLookup();
            for (Iterator it = entries.keySet().iterator(); it.hasNext();) {
                Integer id = (Integer) it.next();
                Integer offset = (Integer) entries.get(id);

                lst.add(offset);
                offset2id.put(offset, id);
            }

            usrOffset = usr.getLastUserEditAtomOffset();
        }
        //sort found records by offset.
        //(it is not necessary but SlideShow.findMostRecentCoreRecords() expects them sorted)
        Object a[] = lst.toArray();
        Arrays.sort(a);
        Record[] rec = new Record[lst.size()];
        for (int i = 0; i < a.length; i++) {
            Integer offset = (Integer) a[i];
            rec[i] = (Record) Record.buildRecordAtOffset(docstream, offset.intValue());
            if (rec[i] instanceof PersistRecord) {
                PersistRecord psr = (PersistRecord) rec[i];
                Integer id = (Integer) offset2id.get(offset);
                psr.setPersistId(id.intValue());
            }
        }

        return rec;
    }

    private void readCurrentUserStream() {
        try {
            currentUser = new CurrentUserAtom(directory);
        } catch (IOException ie) {
            logger.log(POILogger.ERROR, "Error finding Current User Atom:\n" + ie);
            currentUser = new CurrentUserAtom();
        }
    }

    public void write(OutputStream out) throws IOException {
        // Write out, but only the common streams
        write(out, false);
    }

    public void write(OutputStream out, boolean preserveNodes) throws IOException {
    }

    public synchronized int appendRootLevelRecord(Record newRecord) {
        return -1;
    }

    public void addPicture(PictureData img) {
    }

    /* ******************* fetching methods follow ********************* */

    public Record[] getRecords() {
        return new Record[0];
    }

    public byte[] getUnderlyingBytes() {
        return _docstream;
    }

    public CurrentUserAtom getCurrentUserAtom() {
        return currentUser;
    }

    public PictureData[] getPictures() {
        return new PictureData[0];
    }

    public ObjectData[] getEmbeddedObjects() {
        return new ObjectData[0];
    }
}
