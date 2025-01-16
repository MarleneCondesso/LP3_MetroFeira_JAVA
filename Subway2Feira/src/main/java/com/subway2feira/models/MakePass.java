/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.models;

import com.subway2feira.API.API;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.RequestApiTransportPass;
import com.subway2feira.API.models.PassType;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPass;
import com.subway2feira.API.models.TransportPassType;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.Session;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author 35191
 */
public class MakePass {

    API apiConnJSON = new API();
    ArrayList<PassType> passTypes = getPassTypes();
    UserService serviceUser = new UserService();
    AlertBoxController buttonType;

    public void makePassForClient(String email) {

        PassType passType = new PassType();

        // Guarda os dados do cliente num objecto User
        User user = serviceUser.getUserByEmail(email);

        // Verifica se o cliente tem um passe de transporte
        if (user.getGuidPass() == null) {
            Boolean haveTransportPass = false;
            int birthYear = user.getDateBirth().getYear();
            int age = LocalDate.now().getYear() - birthYear;
            
            if (age>=4) {
            
              buttonType = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Passe",
                      " Tem direito a um Passe de Transporte, deseja associa-lo à sua conta?");
              if (buttonType.getAlert().getResult().getText().equals("OK")) {

                  // escolher o passe ideal para o cliente
                  try {
                      passType = selectTypePassIdForClient(user.getDateBirth(), passTypes);
                  } catch (SubwayException ex) {
                      System.out.println(ex.getMessage());
                  }
                  // Cria o passe consoante o que a função anterior realizou
                  createPass(user, passType, user.getDateBirth(), haveTransportPass);
              } else {
                  buttonType.getAlert().close();
              }
            } 
        } else {

            verifyTransportPassIDOfClient(email);
        }

    }

    /**
     * Retorna um array de todos os tipos de passe
     *
     * @return
     */
    private ArrayList<PassType> getPassTypes() {
        TransportPassType typesPass = new TransportPassType();
        // Requisita os tipos de passe existentes
        try {
            typesPass = this.apiConnJSON.getTransportPass();
        } catch (SubwayException ex) {
            System.out.println(ex.getMessage());
        }
        // Guarda os tipos de passe num Array
        ArrayList<PassType> values = typesPass.getTransportPassTypes();

        return values;
    }

    /**
     * Verifica se o passe do cliente está expirado
     *
     * @param email
     */
    private void verifyTransportPassIDOfClient(String email) {

        User user = new User();
        PassType passType = new PassType();

        user = serviceUser.getUserByEmail(email);

        // Verifica qual o tipo de passe de transporte que o cliente tem
        String passTypeClient = getPassClientWithGuidClient(user.getGuidPass()).get(0).getPassTypeId();

        try {
            // Verifica o tipo de passe que o utilizador deve ter
            passType = selectTypePassIdForClient(user.getDateBirth(), passTypes);
        } catch (SubwayException ex) {
            System.out.println(ex.getMessage());
        }

        // Valida se o passe correto é diferente ao que o cliente possui
        if (passType.getPassTypeId() != passTypeClient) {

            buttonType = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Passe",
                    " O seu Passe de Transporte encontra-se expirado ou perto do limite de expiração! \n "
                            + "Tem direito a outro passe correspondente à sua idade. \n"
                            + "Deseja associa-lo à sua conta?");

            if (buttonType.getAlert().getResult().getText().equals("OK")) {
                Boolean haveTransportPass = true;
                // Cria um novo passe caso o cliente aceite ter um novo passe(correto) associado
                // à sua conta.
                createPass(user, passType, user.getDateBirth(), haveTransportPass);

            } else {
                buttonType.getAlert().close();
            }

        }
    }

    /**
     * Retorna um Array do passe de transporte que o cliente tem associado à sua
     * conta.
     *
     * @param guid GUID do Transport Pass
     * @return Um ArrayList<TransporPass>
     */
    private ArrayList<TransportPass> getPassClientWithGuidClient(String guid) {
        ResponseTransportPass passe = new ResponseTransportPass();
        ArrayList<TransportPass> transportPassClient;

        try {
            // Seleciona o passe que o cliente possui
            passe = this.apiConnJSON.getTransportPassesbyID(guid);
        } catch (SubwayException ex) {
            System.out.println(ex.getMessage());
        }
        // Guarda num array Transport Pass
        transportPassClient = passe.getTransportPasses();

        return transportPassClient;
    }

    /**
     * Retorna o passe de transporte correto do cliente
     *
     * @param birthDate Data de Nascimento do Cliente.
     * @param passTypes ArrayList<PassType> dos tipos de Passes de Transporte
     *                  existentes.
     * @return um PassType
     * @throws SubwayException
     */
    private PassType selectTypePassIdForClient(LocalDate birthDate, ArrayList<PassType> passTypes)
            throws SubwayException {

        int yearBirth = birthDate.getYear();

        int age = LocalDate.now().getYear() - yearBirth;

        for (int i = 0; i < passTypes.size(); i++) {
            if (passTypes.get(i).getMinAge() <= age && passTypes.get(i).getMaxAge() >= age) {

                return passTypes.get(i);

            }
        }

        throw new SubwayException("ERRO: Não encontrou um passe dentro dos limites de idade!");
    }

    /**
     * Criação de um novo Passe de transporte
     *
     * @param user              Objeto User
     * @param passTypeClient    Tipo de transport Pass do Cliente
     * @param yearBirth         Data de Nascimento do Cliente
     * @param haveTransportPass True/False caso tenha Transport Pass
     */
    private void createPass(User user, PassType passTypeClient, LocalDate yearBirth, Boolean haveTransportPass) {
        RequestApiTransportPass pass = new RequestApiTransportPass();

        int age = LocalDate.now().getYear() - yearBirth.getYear();

        long diff = passTypeClient.getMaxAge() - age;
       
        LocalDate expirationDate =LocalDate.now().plusYears(diff);
        LocalDate date = LocalDate.of(expirationDate.getYear(), yearBirth.getMonth().getValue(), yearBirth.getDayOfMonth()); 
        
        pass.setClientID(user.getId().toString());
        pass.setPassTypeID(passTypeClient.getPassTypeId());
        pass.setExpirationDate(date.toString());
        pass.setActive(false);

        try {

            // Caso o cliente não tenha transport pass, cria um
            if (!haveTransportPass) {

                ResponseTransportPass passe = this.apiConnJSON.createTransportPass(pass);

                String guid = passe.getTransportPasses().get(0).getId();

                if (passe.getStatus().equals("OK")) {

                    // Atualiza o guid do user na Base de Dados
                    serviceUser.updateGuidByEmail(user.getEmail(), guid);

                    this.apiConnJSON.updateTransportPass(pass, guid);

                    Session.setGuidPass(guid);

                }
                
                // se tiver transport pass faz update ao transpor pass com o transport pass
                // correto
            } else if (haveTransportPass) {

                ResponseTransportPass passe = this.apiConnJSON.createTransportPass(pass);

                this.apiConnJSON.updateTransportPass(pass, passe.getTransportPasses().get(0).getId());


                Session.setGuidPass(passe.getTransportPasses().get(0).getId());

            }

        } catch (SubwayException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
