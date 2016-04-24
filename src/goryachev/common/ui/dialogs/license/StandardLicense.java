// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.license;
import goryachev.common.ui.Application;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.util.TXT;
import javax.swing.text.StyledDocument;


public class StandardLicense
{
	private String author;
	private String supportEmail;
	private boolean militaryClause = true;
	
	
	public StandardLicense()
	{
		author = "Andy Goryachev";
		supportEmail = "support@goryachev.com";
	}
	
	
	public String getTitle()
	{
		return TXT.get("StandardLicense.title", "End-User License Agreement");
	}
	
	
	public void setAuthor(String s)
	{
		author = s;
	}
	
	
	public void setSupportEmail(String s)
	{
		supportEmail = s;
	}
	
	
	public void setMilitaryClause(boolean on)
	{
		militaryClause = on;
	}
	
	
	protected void sp(CDocumentBuilder b)
	{
		b.append("  ");
	}
	
	
	protected void nl(CDocumentBuilder b)
	{
		b.append("\n");
	}
	
	
	protected void a(CDocumentBuilder b, String text)
	{
		b.append(text);
	}
	
	
	protected int addSection(CDocumentBuilder b, int section, String name)
	{
		name = (name == null ? null : section + ". " + name);
		
		b.nl();
		b.nl();

		if(name != null)
		{
			b.append(name);
			b.append("  ");
		}

		section++;
		return section;
	}
	
	
	public StyledDocument getDocument()
	{
		int section = 1;
		
		CDocumentBuilder b = new CDocumentBuilder();
		b.setCenter(true);
		b.setBold(true);
		b.append("END-USER LICENSE AGREEMENT");
		b.setBold(false);
		b.nl().nl();
		b.setCenter(false);
				
		a(b, "IMPORTANT-READ CAREFULLY: This End-User License Agreement (\"EULA\") is a legal agreement between you (either an individual or a single entity) and ");
		a(b, author);
		a(b, " for the ");
		a(b, Application.getTitle());
		a(b, " software product, which includes the User Manual, any associated SOFTWARE components, any media, any printed materials other than the User Manual, and any \"online\" or electronic documentation (\"SOFTWARE\").  ");
		a(b, "By installing, copying, or otherwise using the SOFTWARE, you agree to be bound by the terms of this EULA.  ");
		a(b, "If you do not agree to the terms of this EULA, do not install or use the SOFTWARE.");
		
		section = addSection(b, section, "The SOFTWARE is licensed, not sold.");
		
		section = addSection(b, section, "GRANT OF LICENSE.");
		nl(b);
		nl(b);
		a(b, "(a) Evaluation Configuration.  You may use the evaluation configuration of SOFTWARE without charge for a period of 30 days.  ");
		a(b, "At the end of the evaluation period, you should decide whether you want to keep the Software.  ");
		a(b, "You must purchase a Registration Key if you want to continue using the Software after the evaluation period ends.  ");
		a(b, "If you decide for any reason that you do not want to purchase a Registration Key, you must stop using the Software and remove it from your computer.");
		nl(b);
		nl(b);
		a(b, "(b) Registered Configuration.  ");
		a(b, "After you have purchased the license for SOFTWARE, and have received the Registration Key enabling the registered copy, you are licensed to install the SOFTWARE only on the number of computers corresponding to the number of licenses purchased.");
		
		section = addSection(b, section, "RESTRICTIONS.");
		a(b, "You may not reverse engineer, de-compile, or disassemble the SOFTWARE, except and only to the extent that such activity is expressly permitted by applicable law notwithstanding this limitation.");
		if(militaryClause)
		{
			sp(b);
			b.setBold(true);
			a(b, "You may not use the SOFTWARE in a military or a \"national security\" domain; in the design, construction, operation, or maintenance of weapons.  ");
			b.setBold(false);
			sp(b);
		}
		a(b, "You may not rent, lease, or lend the SOFTWARE.  You may permanently transfer all of your rights under this EULA, provided the recipient agrees to the terms of this EULA."); 
		
		section = addSection(b, section, "SUPPORT SERVICES.");
		a(b, author);
		a(b, " may provide you with support services related to the SOFTWARE.  ");
		a(b, "Use of Support Services is governed by the policy described in the user manual, in online documentation, and/or other provided materials, as they may be modified from time to time.  ");
		a(b, "Any supplemental SOFTWARE code provided to you as part of the Support Services shall be considered part of the SOFTWARE and subject to the terms and conditions of this EULA.");
		
		section = addSection(b, section, "TERMINATION.");
		a(b, "Without prejudice to any other rights, ");
		a(b, author);
		a(b, " may terminate this EULA if you fail to comply with the terms and conditions of this EULA.  ");
		a(b, "In such event, you must destroy all copies of the SOFTWARE.");
		
		section = addSection(b, section, "COPYRIGHT.");
		a(b, "The SOFTWARE is protected by United States copyright law and international treaty provisions.  ");
		a(b, "You acknowledge that no title to the intellectual property in the SOFTWARE is transferred to you.  ");
		a(b, "You further acknowledge that title and full ownership rights to the SOFTWARE will remain the exclusive property of ");
		a(b, author);
		a(b, " and you will not acquire any rights to the SOFTWARE except as expressly set forth in this license.  ");
		a(b, "You agree that any copies of the SOFTWARE will contain the same proprietary notices which appear on and in the SOFTWARE.");
		
		section = addSection(b, section, "EXPORT RESTRICTIONS.");
		a(b, "You agree that you will not export or re-export the SOFTWARE to any country, person, entity, or end user subject to U.S.A. export restrictions.  ");
		a(b, "Restricted countries currently include, but are not necessarily limited to Cuba, Iran, Iraq, Libya, North Korea, Sudan, and Syria.  ");
		a(b, "You warrant and represent that neither the U.S.A. Bureau of Export Administration nor any other federal agency has suspended, revoked or denied your export privileges.");
		
		section = addSection(b, section, "LIMITED WARRANTY.");
		a(b, author);
		a(b, " warrants that the Software will perform substantially in accordance with the accompanying written materials for a period of 90 days from the date of your receipt of the Software.  ");
		a(b, "Any implied warranties on the Software are limited to 90 days.  ");
		a(b, "Some states do not allow limitations on duration of an implied warranty, so the above limitation may not apply to you.  ");
		a(b, author.toUpperCase());
		a(b, " DISCLAIMS ALL OTHER WARRANTIES, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT, WITH RESPECT TO THE SOFTWARE AND THE ACCOMPANYING WRITTEN MATERIALS.  ");
		a(b, "This limited warranty gives you specific legal rights.  ");
		a(b, "You may have others, which vary from state to state.");
		
		section = addSection(b, section, "LIMITATION OF LIABILITY.");
		a(b, "IN NO EVENT SHALL ");
		a(b, author.toUpperCase());
		a(b, " OR ITS SUPPLIERS BE LIABLE TO YOU FOR ANY CONSEQUENTIAL, SPECIAL, INCIDENTAL, OR INDIRECT DAMAGES OF ANY KIND ARISING OUT OF THE DELIVERY, PERFORMANCE, OR USE OF THE SOFTWARE, EVEN IF ");
		a(b, author.toUpperCase());
		a(b, " HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.  ");
		a(b, "IN ANY EVENT, ");
		a(b, author.toUpperCase());
		a(b, "'S LIABILITY FOR ANY CLAIM, WHETHER IN CONTRACT, TORT, OR ANY OTHER THEORY OF LIABILITY WILL NOT EXCEED THE LICENSE FEE PAID BY YOU.");
		
		section = addSection(b, section, "MISCELLANEOUS.");
		a(b, "If you acquired the SOFTWARE in the United States, this EULA is governed by the laws of the state of California.  If you acquired the SOFTWARE outside of the United States, then local laws may apply.");
		
		section = addSection(b, section, null);

		a(b, "If you have any questions concerning this EULA, please send electronic mail to: ");
		a(b, supportEmail);
		a(b, "\n");
		
		return b.getDocument();
	}
}
