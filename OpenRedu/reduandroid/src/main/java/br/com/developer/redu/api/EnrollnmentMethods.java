package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo do Enrollment
 */
public interface EnrollnmentMethods<T> {
	
    /**
     * Retorna a matrícula em questão.
     * 
     * @param enrollmentId - ID da matrícula
     * @return Uma Matricula
     */
    public T getEnrollment(String enrollmentId);
    
    /**
     * Cria uma matrícula no curso em questão. Caso o usuário faça parte do Redu 
     * a matrícula irá para o estado invited e uma notificação aparecerá em sua home
     * perguntando se deseja participar do curso. Caso o usuário não faça parte do Redu, 
     * antes de ir para o estado invited a matrícula passará para um estado redu_invited. 
     * Isso significa que um e-mail de convite foi enviado.
     * 
     * @param courseId - [OBRIGATÓRIO] ID ou path do curso
     * @param email - [OBGRITATÓRIO] E-mail do usuário que será convidado. 
     * Máximo 40 caracteres; deve ser único
     * @return A Matricula criada
     */
    public T postEnrollment(String courseId, String email);

    /**
     * Listagem de matrículas de um curso. É importante perceber que podem 
     * existir mais matrículas do que usuários efetivamente cadastrados no 
     * curso (estado redu_invited ou invited).
     * 
     * @param courseId - ID ou path do curso
     * @return Uma lista de Matriculas
     */
    public List<T> getEnrollmentsByCourse(String courseId);
    
    /**
     * Remove a matrícula especificada bem como o usuário associado a mesma (caso exista). 
     * O usuário perderá acesso ao curso e às suas disciplinas.
     * 
     * @param enrollmentId - ID ou path do curso
     */
    public void deleteEnrollment(String enrollmentId);
    
    public T getEnrollmentUserAtCourse(String userId, String courseId);
    
}
