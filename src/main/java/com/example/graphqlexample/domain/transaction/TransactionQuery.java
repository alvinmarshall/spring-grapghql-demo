package com.example.graphqlexample.domain.transaction;

public class TransactionQuery {
    private TransactionQuery() {
    }

    public static final String SELECT_QUERY = """
            SELECT 
                t.id t_id,
                t.transaction_id t_transaction_id,
                t.fee_amount t_fee_amount,
                t.from_amount t_from_amount,
                t.from_currency t_from_currency,
                t.from_fund_id t_from_fund_id,
                t.bonus_amount t_bonus_amount,
                t.created_at t_created_at,
                t.delivery_status t_delivery_status,
                t.exchange_rate t_exchange_rate,
                t.status t_status,
                t.to_amount t_to_amount,
                t.to_currency t_to_currency,
                t.user_id t_user_id,
                t.sender_id t_sender_id,
                t.fund_source_type t_fund_source_type,
                t.reason t_reason,
                t.reference_number t_reference_number,
                t.payout_reference_number t_payout_reference_number,
                t.coupon_code t_coupon_code,
                t.recipient_id t_recipient_id,
                t.recipient_first_name t_recipient_first_name,
                t.recipient_last_name t_recipient_last_name,
                t.recipient_payout_method t_recipient_payout_method,
                t.coupon_code t_coupon_code
              
             FROM transactions t
            """;

    public static final String COUNT_QUERY = """
            SELECT count(*) as count FROM transactions
            """;
}
