import com.fiap.finpath.auth.*;
import com.fiap.finpath.financial.BankAccountService;
import com.fiap.finpath.financial.BankTransactionService;
import com.fiap.finpath.financial.ExpenseService;
import com.fiap.finpath.financial.IncomeService;
import com.fiap.finpath.gamification.AchievementService;
import com.fiap.finpath.gamification.ChallengeService;
import com.fiap.finpath.gamification.Challenges;
import com.fiap.finpath.gamification.MemberGamificationService;
import com.fiap.finpath.group.GroupMembersService;
import com.fiap.finpath.group.GroupService;
import com.fiap.finpath.organization.MemberService;
import com.fiap.finpath.organization.OrganizationService;

import java.time.Instant;

public class Teste {
    public static void main(String[] args) {
        System.out.println("//--- Starting Finpath Service Test Run ---//");

        // --- Organization Services ---
        System.out.println("\n--- Testing Organization Services ---");
        OrganizationService orgService = new OrganizationService();
        MemberService memberService = new MemberService();

        String orgId = orgService.createOrganization("Finpath Teste", true, Instant.now().getEpochSecond() + 86400 * 90).getId();
        System.out.println("Retrieved Organization ID: " + orgId);
        orgService.getOrganizationById(orgId);
        orgService.updateOrganization(orgId, "Finpath Chofs", true, Instant.now().getEpochSecond() + 86400 * 120);

        String userId = "testUserId123";
        String roleId = "adminRoleId456";
        String memberId = memberService.addMemberToOrganization(orgId, userId, roleId).getId();
        System.out.println("Retrieved Member ID: " + memberId);
        memberService.getMemberById(memberId);
        memberService.getMembersByOrganization(orgId);
        memberService.updateMemberRole(memberId, "userRoleId789");

        // --- Group Services ---
        System.out.println("\n--- Testing Group Services ---");
        GroupService groupService = new GroupService();
        GroupMembersService groupMembersService = new GroupMembersService();

        String groupId = groupService.createGroup(orgId, "Grupo Orcamento Casal", "Um grupo para centralizar o orcamento e economias do casal").getId();
        System.out.println("Retrieved Group ID: " + groupId);
        groupService.getGroupById(groupId);
        groupService.updateGroup(groupId, "Casal", "Teste.");
        groupMembersService.addMemberToGroup(memberId, groupId);
        groupMembersService.isMemberInGroup(memberId, groupId);

        // --- Financial Services ---
        System.out.println("\n--- Testing Financial Services ---");
        BankAccountService bankAccountService = new BankAccountService();
        BankTransactionService bankTransactionService = new BankTransactionService();
        ExpenseService expenseService = new ExpenseService();
        IncomeService incomeService = new IncomeService();

        String bankAccountId = bankAccountService.connectBankAccount(orgId, memberId, "Sicredi", "someAccessToken").getId();
        System.out.println("Retrieved Bank Account ID: " + bankAccountId);
        bankAccountService.getBankAccountById(bankAccountId);
        bankAccountService.syncAccountTransactions(bankAccountId);

        String transactionId = bankTransactionService.createBankTransaction(bankAccountId, Instant.now().getEpochSecond(), 150000L, "DEBIT", "Sic").getId(); // 1500.00
        System.out.println("Retrieved Transaction ID: " + transactionId);
        bankTransactionService.getBankTransactionById(transactionId);

        expenseService.createExpense(orgId, memberId, groupId, transactionId, Instant.now().getEpochSecond(), 5000L, "Groceries", "Weekly grocery run"); // 50.00
        incomeService.createIncome(orgId, memberId, groupId, transactionId, Instant.now().getEpochSecond(), 200000L, "Salary", "Monthly Salary Payment"); // 2000.00
        expenseService.getExpensesByOrganization(orgId);
        incomeService.getIncomesByOrganization(orgId);

        // --- Gamification Services ---
        System.out.println("\n--- Testing Gamification Services ---");
        AchievementService achievementService = new AchievementService();
        ChallengeService challengeService = new ChallengeService();
        MemberGamificationService memberGamificationService = new MemberGamificationService();

        String achievementId = achievementService.createAchievement("First Steps", "Completed your first savings!", "url.to.icon.png").getId();
        System.out.println("Retrieved Achievement ID: " + achievementId);
        achievementService.getAchievementById(achievementId);

        String challengeId = challengeService.createChallenge(achievementId, Challenges.DifficultyLevel.EASY, "Save $100", "Save 100 in a month.", Challenges.ChallengeType.SAVINGS, 100).getId();
        System.out.println("Retrieved Challenge ID: " + challengeId);
        challengeService.getChallengeById(challengeId);

        memberGamificationService.addPointsToMember(memberId, 500);
        memberGamificationService.awardAchievement(memberId, achievementId, Instant.now().getEpochSecond());

        // --- Auth Services ---
        System.out.println("\n--- Testing Auth Services ---");
        AuthService authService = new AuthService();
        RoleService roleService = new RoleService();
        ActionService actionService = new ActionService();
        RolesActionService rolesActionService = new RolesActionService();
        PermissionService permissionService = new PermissionService();

        String userEmail = "test@example.com";
        authService.registerUser("Test User", userEmail, "securePassword123");
        authService.loginUser(userEmail, "securePassword123");
        VerificationCode vfCode = authService.requestVerificationCode(userEmail);
        authService.verifyUser(userEmail, vfCode.getCodeText());

        String newRoleId = roleService.createRole("Financial Manager", "Manages financial data").getId();
        System.out.println("Retrieved Role ID: " + newRoleId);
        roleService.getRoleById(newRoleId);

        String newActionId = actionService.createAction("expense_delete", "Permission to delete expenses").getId();
        System.out.println("Retrieved Action ID: " + newActionId);
        actionService.getActionById(newActionId);

        rolesActionService.assignActionToRole(newRoleId, newActionId);
        permissionService.hasPermission(memberId, orgId, "expense_delete");

        System.out.println("\n//--- Finpath Service Test Run Complete ---//");
    }
}