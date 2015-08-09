package com.ritterliu.angryBirdEleven;

public class MyMatrix {
	/** 
     * 1   
     * ����������ʽ ���룺ԭʼ����+��+�� ��ʵ���������к�����Ŀ 
     */  
    public static float[][] getDY(float[][] data, int h, int v) {  
        int H = data.length;  
        int V = data[0].length;  
        float[][] newData = new float[H - 1][V - 1];  
  
        for (int i = 0; i < newData.length; i++) {  
  
            if (i < h - 1) {  
                for (int j = 0; j < newData[i].length; j++) {  
                    if (j < v - 1) {  
                        newData[i][j] = data[i][j];  
                    } else {  
                        newData[i][j] = data[i][j + 1];  
                    }  
                }  
            } else {  
                for (int j = 0; j < newData[i].length; j++) {  
                    if (j < v - 1) {  
                        newData[i][j] = data[i + 1][j];  
                    } else {  
                        newData[i][j] = data[i + 1][j + 1];  
                    }  
                }  
  
            }  
        }  
  
        return newData;  
    }  

    /** 
     * �������ʽ��ģ----------->���յ��ܽ���� 
     *  
     * @param data 
     * @return 
     */  
    public static float getHL(float[][] data) {  
  
        // ��ֹ����  
        if (data.length == 2) {  
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];  
        }  
  
        float total = 0;  
        // ����data �õ�����ʽ������������  
        int num = data.length;  
        // ����һ����СΪnum �������Ŷ�Ӧ��չ������Ԫ����ĵ�ֵ  
        float[] nums = new float[num];  
  
        for (int i = 0; i < num; i++) {  
            if (i % 2 == 0) {  
                nums[i] = data[0][i] * getHL(getDY(data, 1, i + 1));  
            } else {  
                nums[i] = -data[0][i] * getHL(getDY(data, 1, i + 1));  
            }  
        }  
        for (int i = 0; i < num; i++) {  
            total += nums[i];  
        }  
   //     System.out.println("total=" + total);  
        return total;  
    }  
    
    /** 
     * ��������------>z�����ܽ�͹��� 
     *  
     * @param data 
     * @return 
     */  
    public static float[][] getN(float[][] data) {  
        // �����������ʽ��ģ|data|  
        float A = getHL(data);  
        // ����һ���������������  
        float[][] newData = new float[data.length][data.length];  
  
        for (int i = 0; i < data.length; i++) {  
            for (int j = 0; j < data.length; j++) {  
                float num;  
                if ((i + j) % 2 == 0) {  
                    num = getHL(getDY(data, i + 1, j + 1));  
                } else {  
                    num = -getHL(getDY(data, i + 1, j + 1));  
                }  
  
                newData[i][j] = num / A;  
            }  
        }  
  
        // ת�� ��������ʽת��  
        newData = getA_T(newData);  
  
        return newData;  
    }  
    
    /** 
     * ȡ��ת�þ��� 
     * @param A 
     * @return 
     */  
    public static float[][] getA_T(float[][] A) {  
        int h = A.length;  
        int v = A[0].length;  
        // ������A�к����෴��ת�þ���  
        float[][] A_T = new float[v][h];  
        // ����Aȡ��ת�þ���A_T  
        for (int i = 0; i < v; i++) {  
            for (int j = 0; j < h; j++) {  
                A_T[j][i] = A[i][j];  
            }  
        }  
    //    System.out.println("ȡ��ת�þ���  wanbi........");  
        return A_T;  
    }  
	
}