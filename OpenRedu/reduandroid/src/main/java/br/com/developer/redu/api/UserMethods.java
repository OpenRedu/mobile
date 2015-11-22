package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo do usuário.
 */
public interface UserMethods<T> {
	
    /**
     * Retorna um único usuário com o ID ou login especificado. Tanto o login quanto 
     * o ID servem como identificadores únicos para os usuários, 
     * por isso podem ser passados como parâmetro aqui.
     * 
     * @param userId - ID ou login do usuário em questão
     * @return Um usuário.
     */
    public T getUser(String userId);

    /**
     * Retorna uma representação do resource owner. 
     * Resource owner é usuário que autorizou a aplicação a utilizar seus dados.
     * 
     * @return Um usuário
     */
    public T getMe();

    /**
     * Retorna todos os usuários cuja matrícula tem o estado approved no 
     * curso ao qual a disciplina está associada. Opcionalmente é possível 
     * filtrar os usuários por papeis.
     * 
     * @param spaceId - ID da Disciplina.
     * @param role - Papel do usuário no curso. 
     * Pode ser um dos seguintes papéis: member, teacher, environment_admin, tutor
     * @return Uma lista de usuários
     */
    public List<T> getUsersBySpace(String spaceId, String role);


}
