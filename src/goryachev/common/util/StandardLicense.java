// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** Generates a standard End User License Agreement */
public abstract class StandardLicense
{
	protected abstract void bold(boolean on);
	
	protected abstract void nl();
	
	protected abstract void text(String text);
	
	protected abstract void title(boolean on);
	
	//
	
	private final String application;
	private final String author;
	private final String supportEmail;
	private final boolean militaryClause;
	
	
	public StandardLicense(String application, String author, String supportEmail, boolean militaryClause)
	{
		this.application = application;
		this.author = author;
		this.supportEmail = supportEmail;
		this.militaryClause = militaryClause;
	}
	
	
	protected int addSection(int section, String name)
	{
		name = (name == null ? null : section + ". " + name);
		
		nl();
		nl();

		if(name != null)
		{
			text(name);
			text("  ");
		}

		section++;
		return section;
	}
	
	
	public void generate()
	{
		int section = 1;
		
		title(true);
		text("END-USER LICENSE AGREEMENT");
		title(false);
		nl();
		nl();
				
		text("IMPORTANT-READ CAREFULLY: This End-User License Agreement (\"EULA\") is a legal agreement between you (either an individual or a single entity) and ");
		text(author);
		text(" for the ");
		text(application);
		text(" software product, which includes the User Manual, any associated SOFTWARE components, any media, any printed materials other than the User Manual, and any \"online\" or electronic documentation (\"SOFTWARE\").  ");
		text("By installing, copying, or otherwise using the SOFTWARE, you agree to be bound by the terms of this EULA.  ");
		text("If you do not agree to the terms of this EULA, do not install or use the SOFTWARE.");
		
		section = addSection(section, "The SOFTWARE is licensed, not sold.");
		
		section = addSection(section, "GRANT OF LICENSE.");
		nl();
		nl();
		text("(a) Evaluation Configuration.  You may use the evaluation configuration of SOFTWARE without charge for a period of 30 days.  ");
		text("At the end of the evaluation period, you should decide whether you want to keep the Software.  ");
		text("You must purchase a Registration Key if you want to continue using the Software after the evaluation period ends.  ");
		text("If you decide for any reason that you do not want to purchase a Registration Key, you must stop using the Software and remove it from your computer.");
		nl();
		nl();
		text("(b) Registered Configuration.  ");
		text("After you have purchased the license for SOFTWARE, and have received the Registration Key enabling the registered copy, you are licensed to install the SOFTWARE only on the number of computers corresponding to the number of licenses purchased.");
		
		section = addSection(section, "RESTRICTIONS.");
		text("You may not reverse engineer, de-compile, or disassemble the SOFTWARE, except and only to the extent that such activity is expressly permitted by applicable law notwithstanding this limitation.");
		if(militaryClause)
		{
			text("  ");
			bold(true);
			text("You may not use the SOFTWARE in a military or a \"national security\" domain; in the design, construction, operation, or maintenance of weapons.  ");
			bold(false);
			text("  ");
		}
		text("You may not rent, lease, or lend the SOFTWARE.  You may permanently transfer all of your rights under this EULA, provided the recipient agrees to the terms of this EULA."); 
		
		section = addSection(section, "SUPPORT SERVICES.");
		text(author);
		text(" may provide you with support services related to the SOFTWARE.  ");
		text("Use of Support Services is governed by the policy described in the user manual, in online documentation, and/or other provided materials, as they may be modified from time to time.  ");
		text("Any supplemental SOFTWARE code provided to you as part of the Support Services shall be considered part of the SOFTWARE and subject to the terms and conditions of this EULA.");
		
		section = addSection(section, "TERMINATION.");
		text("Without prejudice to any other rights, ");
		text(author);
		text(" may terminate this EULA if you fail to comply with the terms and conditions of this EULA.  ");
		text("In such event, you must destroy all copies of the SOFTWARE.");
		
		section = addSection(section, "COPYRIGHT.");
		text("The SOFTWARE is protected by United States copyright law and international treaty provisions.  ");
		text("You acknowledge that no title to the intellectual property in the SOFTWARE is transferred to you.  ");
		text("You further acknowledge that title and full ownership rights to the SOFTWARE will remain the exclusive property of ");
		text(author);
		text(" and you will not acquire any rights to the SOFTWARE except as expressly set forth in this license.  ");
		text("You agree that any copies of the SOFTWARE will contain the same proprietary notices which appear on and in the SOFTWARE.");
		
		section = addSection(section, "EXPORT RESTRICTIONS.");
		text("You agree that you will not export or re-export the SOFTWARE to any country, person, entity, or end user subject to U.S.A. export restrictions.  ");
		text("Restricted countries currently include, but are not necessarily limited to Cuba, Iran, Iraq, Libya, North Korea, Sudan, and Syria.  ");
		text("You warrant and represent that neither the U.S.A. Bureau of Export Administration nor any other federal agency has suspended, revoked or denied your export privileges.");
		
		section = addSection(section, "LIMITED WARRANTY.");
		text(author);
		text(" warrants that the Software will perform substantially in accordance with the accompanying written materials for a period of 90 days from the date of your receipt of the Software.  ");
		text("Any implied warranties on the Software are limited to 90 days.  ");
		text("Some states do not allow limitations on duration of an implied warranty, so the above limitation may not apply to you.  ");
		text(author.toUpperCase());
		text(" DISCLAIMS ALL OTHER WARRANTIES, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT, WITH RESPECT TO THE SOFTWARE AND THE ACCOMPANYING WRITTEN MATERIALS.  ");
		text("This limited warranty gives you specific legal rights.  ");
		text("You may have others, which vary from state to state.");
		
		section = addSection(section, "LIMITATION OF LIABILITY.");
		text("IN NO EVENT SHALL ");
		text(author.toUpperCase());
		text(" OR ITS SUPPLIERS BE LIABLE TO YOU FOR ANY CONSEQUENTIAL, SPECIAL, INCIDENTAL, OR INDIRECT DAMAGES OF ANY KIND ARISING OUT OF THE DELIVERY, PERFORMANCE, OR USE OF THE SOFTWARE, EVEN IF ");
		text(author.toUpperCase());
		text(" HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.  ");
		text("IN ANY EVENT, ");
		text(author.toUpperCase());
		text("'S LIABILITY FOR ANY CLAIM, WHETHER IN CONTRACT, TORT, OR ANY OTHER THEORY OF LIABILITY WILL NOT EXCEED THE LICENSE FEE PAID BY YOU.");
		
		section = addSection(section, "MISCELLANEOUS.");
		text("If you acquired the SOFTWARE in the United States, this EULA is governed by the laws of the state of California.  If you acquired the SOFTWARE outside of the United States, then local laws may apply.");
		
		section = addSection(section, null);

		text("If you have any questions concerning this EULA, please send electronic mail to: ");
		text(supportEmail);
		nl();
	}
}
