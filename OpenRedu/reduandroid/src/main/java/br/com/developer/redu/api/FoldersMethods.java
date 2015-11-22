package br.com.developer.redu.api;

import java.util.List;

/**
 * @author rgcrocha
 *
 * @param <T> - Tipo do Space
 */
public interface FoldersMethods<T> {

	/**
	 * Retorna as pastas da disciplina em questão.
	 * @param spaceId - ID da disciplina
	 */
	public List<T> getFoldersBySpace(String spaceId);
	public String getFolderID(String spaceId);
	public T getFolder(String folderId);
	public List<T> getFolders(String folderId);
	public T postFolder(String name, String folderId);
	public void deleteFolder(String folderId);
	public void editFolder(String name, String folderId);


}
