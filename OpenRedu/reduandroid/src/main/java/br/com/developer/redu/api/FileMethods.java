package br.com.developer.redu.api;

import java.io.IOException;
import java.util.List;

import br.com.developer.redu.models.File;

/**
 * @author thiagosoara
 * 
 * 
 */
public interface FileMethods<T> {

	/**
	 * Retorna os arquivos da pasta em questão.
	 * 
	 * @param folderId
	 *            - ID pasta
	 */
	public List<T> getFilesByFolder(String folderId);

	public T postFile(String folderId, java.io.File file) throws IOException;

	public void deleteFile(String fileId);

}
