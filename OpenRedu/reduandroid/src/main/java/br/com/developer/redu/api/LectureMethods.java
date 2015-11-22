package br.com.developer.redu.api;

import java.io.IOException;
import java.util.List;

import br.com.developer.redu.models.Lecture;

/**
 * @author igor
 * 
 * @param <T>
 *            - Tipo do Subject
 */
public interface LectureMethods<T> {

	/**
	 * Retorna a disciplina em questão.
	 * 
	 * @param lectureId
	 *            - ID da disciplina
	 * @return Uma disciplina
	 */
	public T getLecture(String lectureId);

	public List<T> getLecturesBySubject(String subjectId);

	public void removeLecture(String lectureId);

	/**
	 * Retorna os módulos de uma disciplina.
	 * 
	 * @param spaceId
	 *            - ID do módulo
	 * @return Uma lista de módulos
	 */

	// public List<T> getSubjectsBySubject(String subjectId);

	/* *//**
	 * Cria um módulo no contexto da disciplina especificada por spaceId.
	 * 
	 * @param spaceId
	 *            - [OBRIGATÓRIO] ID da disciplina.
	 * @param title
	 *            - [OBRIGATÓRIO] Nome do módulo. Máximo 40 caracteres.
	 * @param description
	 *            - Descrição do módulo. Máximo 250 caracteres
	 * @return O módulo criado.
	 */
	public T postLecture(Lecture lecture, String spaceId, java.io.File file) throws IOException;

	/**
	 * Atualiza as informações do módulo.
	 * 
	 * @param subjectId
	 *            - ID da disciplina.
	 * @param title
	 *            - Nome do módulo. Máximo 40 caracteres.
	 * @param description
	 *            - Descrição do módulo. Máximo 250 caracteres
	 * @return O módulo editado.
	 */
	public void editSubject(String subjectId, String title, String description);

	/**
	 * Remove um módulo. Os que fazem parte do módulo não terão mais acesso
	 * às aulas.
	 * 
	 * @param subjectId
	 *            - ID do módulo
	 */
	/*
	 * public void deleteSubject(String subjectId);
	 */

}
