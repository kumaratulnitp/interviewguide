# DBMS Interview Questions

Questions and concise answer points for computer-science graduates. Be ready to relate concepts to a small schema such as `Student`, `Course`, and `Enrollment`.

## Foundations and Design

1. **What is a DBMS? Why use one instead of files?**
   - A DBMS provides structured storage, query languages, concurrency control, recovery, integrity constraints, security, and reduced duplication.
   - Files can work for a small, single-user application, but they make concurrent writes, querying related data, enforcing rules, and recovering after failure the application's responsibility. A DBMS centralizes those concerns and gives applications a shared data model.

2. **What are primary, candidate, super, foreign, and composite keys?**
   - A superkey uniquely identifies a row. A candidate key is a minimal superkey. One candidate key becomes the primary key. A foreign key references a key in another table. A composite key uses multiple columns.

3. **What is normalization? Why is it useful?**
   - Normalization organizes relations using dependencies to reduce update, insertion, and deletion anomalies while preserving meaningful data relationships.
   - For example, storing a department's name beside every employee duplicates the same fact. A department rename then needs many updates; placing department data in its own relation means one update changes the authoritative value.

4. **Explain 1NF, 2NF, 3NF, and BCNF.**
   - **1NF** uses atomic values: one cell should hold one value, and repeating groups should become rows. For example, `Student(student_id, name, phone_numbers)` breaks 1NF if `phone_numbers` contains `9876, 9123`; use a separate `StudentPhone(student_id, phone_number)` table instead.
   - **2NF** applies when a table has a composite key. Every non-key column must depend on the *whole* key, not just part of it. For example, in `Enrollment(student_id, course_id, student_name, course_name)`, the key might be `(student_id, course_id)`, but `student_name` depends only on `student_id` and `course_name` only on `course_id`. Move student and course details to their own tables.
   - **3NF** removes transitive dependencies: a non-key column should not depend on another non-key column. For example, `Employee(employee_id, employee_name, department_id, department_name)` has `department_id -> department_name`. Store `department_name` in `Department` and keep only `department_id` in `Employee`.
   - **BCNF** is stricter: every column (or set of columns) that determines another column must be a candidate key. It handles a few edge cases that can remain in 3NF.
   - In practice, normalize first to protect correctness, then evaluate measured performance needs. BCNF is stricter than 3NF and can occasionally sacrifice dependency preservation, so 3NF is often a practical design target.

5. **When might denormalization be appropriate?**
   - It can improve read performance or simplify reporting when duplication is controlled and the cost of maintaining consistency is accepted.
   - For example, an order screen regularly needs the total of thousands of order items. Instead of calculating `SUM(quantity * unit_price)` every time, an `Orders` table may store `order_total`. When an item is added, updated, or removed, the same transaction must update `order_total`; otherwise the displayed total can become incorrect.
   - Other examples include a nightly sales-summary table for dashboards and a copied display name in a search index. The design needs a clear refresh or transaction strategy; otherwise the faster read path risks serving inconsistent data.

## SQL and Querying

6. **Compare `WHERE` and `HAVING`.**
   - `WHERE` filters rows before grouping; `HAVING` filters groups after aggregation.
   - Prefer `WHERE` when possible because it reduces rows before grouping. For example, use `WHERE status = 'PAID'` before `GROUP BY customer_id`, but use `HAVING COUNT(*) > 5` to filter the resulting groups.

7. **Compare `INNER`, `LEFT`, `RIGHT`, and `FULL OUTER` joins.**
   - An inner join returns matches only. Outer joins retain unmatched rows from the left, right, or both inputs, filling missing columns with `NULL`.
   - Using the `Employees` and `Departments` data below, Ana, Ben, Chen, and Divya have a matching department; Esha has no department; and Legal has no employee. Joining on `employee.department_id = department.department_id` produces:

     | Join | Result |
     | --- | --- |
     | `INNER JOIN` | The four matched employee--department pairs; Esha and Legal are omitted. |
     | `LEFT JOIN` | Every employee, including Esha--`NULL`. |
     | `RIGHT JOIN` | Every department, including Legal--`NULL`. |
     | `FULL OUTER JOIN` | All matched pairs, plus Esha--`NULL` and `NULL`--Legal. |

   - `LEFT JOIN` is usually easier to read than `RIGHT JOIN` because it keeps the primary table first. A common pitfall is putting a condition on the right table in `WHERE`: this removes null-extended rows and can accidentally turn a left join into an inner join.

8. **What is the difference between `DELETE`, `TRUNCATE`, and `DROP`?**
   - `DELETE` removes selected rows and is typically logged row by row. `TRUNCATE` removes all rows efficiently and commonly resets storage metadata. `DROP` removes the table definition itself. Exact transactional behavior is DBMS-specific.
   - Use `DELETE` when a predicate, row-level trigger behavior, or controlled batch removal is needed. Never assume `TRUNCATE` has identical locking, rollback, identity-reset, or foreign-key behavior across database products.

9. **What are correlated subqueries and common table expressions (CTEs)?**
   - A correlated subquery refers to values from its outer query. A CTE names a temporary query result for one statement and can improve readability; recursive CTEs express hierarchies.
   - A correlated subquery is conceptually evaluated in the context of each outer row, although the optimizer may rewrite it as a join. A CTE improves organization, but it is not automatically faster; some engines materialize it while others inline it.

10. **What are window functions?**
   - They calculate across a related set of rows without collapsing it, for example `ROW_NUMBER()`, running totals, or rank within each department.
   - Unlike `GROUP BY`, window functions preserve each input row. The `OVER` clause defines the window: `PARTITION BY` divides rows into groups, `ORDER BY` defines sequence, and a frame can limit which nearby rows participate.

## Indexes and Query Performance

11. **What is an index? What are its trade-offs?**
   - An index is an auxiliary structure that speeds lookups, joins, or ordering. It consumes storage and adds write cost because inserts, updates, and deletes must maintain it.
   - An index helps only when its access path is cheaper than reading the table. Good candidates are selective filter columns, join keys, and common sort keys; indexing every column can slow writes and confuse the optimizer.

12. **Compare clustered and non-clustered indexes.**
   - A clustered index determines or closely aligns with physical row order, so there is generally one per table. A non-clustered index stores keys and row locators separately; there can be many. Details vary by DBMS.
   - Because a clustered order places related rows near each other, it is particularly useful for range scans. A non-clustered lookup may require a second lookup to fetch non-indexed columns, unless the index covers the query.

13. **Why are B+ trees commonly used for database indexes?**
   - Their high branching factor minimizes disk/page reads, and ordered leaf nodes support efficient equality and range queries.
   - Databases work in pages, not individual bytes. A B+ tree node can hold many keys and child pointers in one page, so a very large index is only a few levels deep; each level often costs one page read if it is not cached. All records or row pointers are at the leaf level, and leaves are linked in key order, making `BETWEEN`, prefix, and ordered scans efficient. This balance makes B+ trees a strong general-purpose choice; hash indexes are often faster for equality-only lookups but do not naturally support ranges.

14. **What is an execution plan?**
   - It is the optimizer's chosen operations and access paths, such as scans, joins, sorts, and estimated row counts. Use it to diagnose slow SQL before adding indexes blindly.
   - Compare estimated and actual row counts when the DBMS exposes both. Large differences often point to stale statistics, skewed data, or predicates the optimizer cannot estimate well, which can lead to a poor join order or access method.

15. **Why can an index be ignored?**
   - The optimizer may estimate a table scan as cheaper, statistics may be stale, predicates may be non-sargable, selectivity may be poor, or the index may not match the query's leading columns.
   - A non-sargable predicate applies a function to the indexed column, such as `LOWER(email) = ...`; a normal index may then be unusable. Rewriting the condition or adding a suitable functional index can help, but only after confirming the plan and workload.

## Transactions and Concurrency

16. **Explain ACID.**
   - Atomicity commits all or none of a transaction. Consistency preserves declared rules. Isolation controls interference among concurrent transactions. Durability keeps committed changes after failures.
   - Consistency is partly the database's job (constraints and transactions) and partly the application's job (correct business rules). ACID does not mean every distributed system gives globally strong consistency at every moment.

17. **What are dirty reads, non-repeatable reads, and phantom reads?**
   - A dirty read observes uncommitted data. A non-repeatable read sees a changed row on re-read. A phantom read sees additional or missing rows that match a repeated predicate.
   - Example: one transaction counts employees in a department twice while another inserts a matching employee between the counts; the extra row is a phantom. The anomalies allowed depend on the DBMS and isolation mode.

18. **What are the usual isolation levels?**
   - Read Uncommitted, Read Committed, Repeatable Read, and Serializable. Higher isolation prevents more anomalies but can reduce concurrency; implementation details differ across databases.
   - Do not memorize guarantees without naming the database: MVCC implementations can provide stronger practical behavior than the SQL standard wording. Use Serializable when correctness requires transactions to behave as if they ran one at a time, and handle possible serialization retries.

19. **What is a database deadlock?**
   - Transactions wait cyclically for locks. The DBMS detects or times out one transaction and rolls it back. Consistent lock ordering and short transactions reduce the risk.
   - For instance, transaction A locks row 1 then requests row 2, while B locks row 2 then requests row 1. Applications should treat a deadlock victim error as retryable when the transaction is safe to retry.

20. **What are locks and MVCC?**
   - Locks coordinate conflicting access directly. Multi-Version Concurrency Control keeps row versions so readers can often use a consistent snapshot without blocking writers.
   - MVCC improves read/write concurrency, but old versions must be cleaned up and write-write conflicts still require coordination. Long-running transactions can prevent cleanup and increase storage pressure.

## Recovery and Distributed Data

21. **What is write-ahead logging (WAL)?**
   - Before a changed data page reaches durable storage, enough log information to redo or undo the change is persisted. WAL supports crash recovery.
   - On recovery, the database can replay committed changes that did not reach data pages and undo incomplete work. A commit acknowledgment normally requires the relevant log record to be durable, not every changed table page.

22. **What are checkpoints?**
    - Checkpoints record a recovery point and flush or track dirty state, reducing the amount of log work required after a crash.

23. **What is replication? What is sharding?**
   - Replication copies data across nodes for availability and read scaling. Sharding partitions rows across nodes for storage and write scaling; it complicates routing, joins, and transactions.
   - Replicas may be synchronous or asynchronous; asynchronous replication can expose stale reads after a write. A shard key should distribute load evenly and keep common queries local, since cross-shard operations are expensive.

24. **What is the CAP trade-off?**
   - During a network partition, a distributed system cannot simultaneously guarantee both strong consistency and availability. CAP applies specifically in the presence of partitions.
   - In CAP, consistency means all clients observe a single, up-to-date value, and availability means every non-failing node returns a response. It is not a daily three-way tuning knob: the decisive trade-off appears when a partition actually occurs.

## Common SQL Prompts and Answers

The following beginner-friendly questions all refer to these two tables. The tables are listed as data only, so you can focus on reading the queries. SQL dialects differ slightly; for example, some databases use `FETCH FIRST` instead of `LIMIT`.

### `Departments`

| department_id | department_name | location |
| ---: | --- | --- |
| 10 | Engineering | Bengaluru |
| 20 | HR | Mumbai |
| 30 | Finance | Delhi |
| 40 | Legal | Chennai |

### `Employees`

| employee_id | employee_name | department_id | salary | city |
| ---: | --- | ---: | ---: | --- |
| 101 | Ana | 10 | 70000 | Bengaluru |
| 102 | Ben | 20 | 55000 | Mumbai |
| 103 | Chen | 10 | 65000 | Pune |
| 104 | Divya | 30 | 60000 | Delhi |
| 105 | Esha | `NULL` | 50000 | Bengaluru |

`Employees.department_id` refers to `Departments.department_id`. Esha is intentionally not assigned to a department, and Legal intentionally has no employee; together they make outer-join results easier to see.

### 1. List every employee

```sql
SELECT employee_id, employee_name, salary
FROM Employees;
```

### 2. Find employees whose salary is greater than 60,000

```sql
SELECT employee_name, salary
FROM Employees
WHERE salary > 60000;
```

### 3. List employees from Bengaluru, ordered by name

```sql
SELECT employee_name, city
FROM Employees
WHERE city = 'Bengaluru'
ORDER BY employee_name;
```

### 4. Find the highest salary

`MAX` returns one value: the largest salary in the table.

```sql
SELECT MAX(salary) AS highest_salary
FROM Employees;
```

### 5. Count the employees in each department

`GROUP BY` creates one group for each department ID. The `COUNT` then counts the rows in each group.

```sql
SELECT department_id, COUNT(*) AS employee_count
FROM Employees
WHERE department_id IS NOT NULL
GROUP BY department_id;
```

### 6. Show each employee with their department name

Use an inner join when you want only employees that have a matching department. Esha is not returned because her department is `NULL`.

```sql
SELECT e.employee_name, d.department_name
FROM Employees AS e
INNER JOIN Departments AS d
    ON e.department_id = d.department_id;
```

### 7. Show every employee, including an employee with no department

Use a left join to retain every row from `Employees`. Esha is returned with a `NULL` department name.

```sql
SELECT e.employee_name, d.department_name
FROM Employees AS e
LEFT JOIN Departments AS d
    ON e.department_id = d.department_id;
```

### 8. Find employees who do not have a department

```sql
SELECT employee_name
FROM Employees
WHERE department_id IS NULL;
```

### 9. Find employees paid above the average salary

The inner query first calculates one average salary. The outer query compares every employee to that value.

```sql
SELECT employee_name, salary
FROM Employees
WHERE salary > (
    SELECT AVG(salary)
    FROM Employees
);
```
