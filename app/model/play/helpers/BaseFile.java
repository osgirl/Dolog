package model.play.helpers;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.dolan.tools.LogTool;

/**
 * The Class BaseFile.
 */
public abstract class BaseFile implements IFileWrapper {
	
	/** The name. */
	protected String name;
	
	/** The file type. */
	protected String fileType;
	
	/** The id. */
	protected String id;
	
	/** The size. */
	protected long size;

	/**
	 * Instantiates a new base file.
	 *
	 * @param name the name
	 */
	public BaseFile(String name) {
		Objects.requireNonNull(name, "Name cannot be null");
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be blank");
		}
		LogTool.traceC(this.getClass(), "Creating File", name);
		this.name = name;
		this.fileType = FilenameUtils.getExtension(name);
		this.id = generateID();
	}

	/**
	 * Generate id.
	 *
	 * @return the string
	 */
	protected String generateID() {
		return UUID.randomUUID().toString();
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getFileType()
	 */
	@Override
	public String getFileType() {
		return this.fileType;
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getID()
	 */
	@Override
	public String getID() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getSize()
	 */
	@Override
	public long getSize() {
		return this.size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.id;
	}
}
