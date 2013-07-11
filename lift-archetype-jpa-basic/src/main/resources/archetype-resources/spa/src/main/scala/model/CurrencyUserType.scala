/*
 * Copyright 2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ${package}
package model

import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

import org.hibernate.HibernateException
import org.hibernate.usertype.UserType
import org.hibernate.engine.spi.SessionImplementor

/**
 * Helper class to translate money amount for hibernate
 */
abstract class CurrencyUserType[CZ <: CurrencyZone](cz: CZ) extends UserType {

  type MyCurrency = CZ#Currency

  val SQL_TYPES = Array(Types.NUMERIC)

  override def sqlTypes() = SQL_TYPES

  override def returnedClass = cz.getClass

  override def equals(x: Object, y: Object): Boolean = {
    if (x == null || y == null) false
    else x == y
  }

  override def hashCode(x: Object) = x.hashCode

  override def nullSafeGet(resultSet: ResultSet, names: Array[String], sessionImplementor: SessionImplementor, owner: Object) : Object = {
    val dollarVal = resultSet.getBigDecimal(names(0))
    if (resultSet.wasNull()) cz.make(0)
    else cz.make(new BigDecimal(dollarVal))
  }

  override def nullSafeSet(statement: PreparedStatement, value: Object, index: Int, sessionImplementor: SessionImplementor) {
    if (value == null) {
      statement.setNull(index, Types.NUMERIC)
    } else {
      val dollarVal = value.asInstanceOf[MyCurrency]
      statement.setBigDecimal(index, dollarVal.amount.bigDecimal)
    }
  }

  override def deepCopy(value: Object): Object = value

  override def isMutable = false

  override def disassemble(value: Object) = value.asInstanceOf[Serializable]

  override def assemble(cached: Serializable, owner: Object): Object = cached.asInstanceOf[Object]

  override def replace(original: Object, target: Object, owner: Object) = original

}
