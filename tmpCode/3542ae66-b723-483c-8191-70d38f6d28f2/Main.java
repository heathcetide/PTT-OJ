import java.util.Scanner;

public class MaxSubarraySum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取整数 K
        int K = scanner.nextInt();
        scanner.nextLine(); // 读取换行符

        // 读取 K 个整数
        String[] numbersStr = scanner.nextLine().split(" ");
        int[] numbers = new int[K];
        for (int i = 0; i < K; i++) {
            numbers[i] = Integer.parseInt(numbersStr[i]);
        }

        // 计算最大子列和
        int maxSum = findMaxSubarraySum(numbers);

        // 输出结果
        System.out.println(maxSum);
    }

    /**
     * 使用 Kadane 算法找到最大子列和
     *
     * @param nums 给定的整数序列
     * @return 最大子列和
     */
    private static int findMaxSubarraySum(int[] nums) {
        int maxSoFar = 0;
        int maxEndingHere = 0;

        for (int num : nums) {
            maxEndingHere += num;
            if (maxEndingHere < 0) {
                maxEndingHere = 0;
            }
            if (maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
            }
        }

        return maxSoFar;
    }
}
