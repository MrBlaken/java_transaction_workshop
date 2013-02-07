package davenkin.step1_failure;

import davenkin.BankService;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: davenkin
 * Date: 2/5/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class FailureBankService implements BankService{
    private FailureBankDao failureBankDao;
    private FailureInsuranceDao failureInsuranceDao;
    private BasicDataSource dataSource;

    public FailureBankService(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void transfer(int fromId, int toId, int amount) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            failureBankDao.withdraw(fromId, amount);
            failureInsuranceDao.deposit(toId, amount);

            connection.commit();
        } catch (Exception e) {
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void setFailureBankDao(FailureBankDao failureBankDao) {
        this.failureBankDao = failureBankDao;
    }

    public void setFailureInsuranceDao(FailureInsuranceDao failureInsuranceDao) {
        this.failureInsuranceDao = failureInsuranceDao;
    }
}
