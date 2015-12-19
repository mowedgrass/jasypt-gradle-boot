package com.github.mowedgrass.jasyptgradleboot.task.file.resource

class FileSystemWrapper {

    public BufferedWriter getWriter(File file) {
        file.newWriter()
    }

    public BufferedReader getReader(File file) {
        file.newReader()
    }

    public File createFrom(File file, String extension) {
        new File(file.path + extension)
    }

    public void move(File from, File to) {
        from.renameTo(to)
    }

    public void backup(File file, String extension) {
        def backup = new File(file.path + extension)
        backup.delete()
        file.renameTo(backup)
    }

    public void remove(File file) {
        file.delete()
    }
}
